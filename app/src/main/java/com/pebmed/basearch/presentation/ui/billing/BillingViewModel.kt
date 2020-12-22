package com.pebmed.basearch.presentation.ui.billing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.billing.*
import br.com.pebmed.domain.extensions.SupportedDateFormat
import br.com.pebmed.domain.extensions.toDate
import br.com.pebmed.domain.extensions.toSupportedDateFormat
import br.com.pebmed.domain.usecases.*
import com.android.billingclient.api.SkuDetails
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.basearch.presentation.ui.billing.state.PlansViewState
import com.pebmed.basearch.presentation.ui.billing.state.UserStatusViewState
import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import com.pebmed.platform.billing.GooglePlayBillingResponseCodeModel
import com.pebmed.platform.billing.GooglePlayBillingType
import com.pebmed.platform.billing.toPlanModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class BillingViewModel(
    private val googlePlayBillingClientWrapper: GooglePlayBillingClientWrapper,
    private val getPlansUseCase: GetPlansUseCase,
    private val validatePurchasedStorePlanUseCase: ValidatePurchasedStorePlanUseCase,
    private val savePurchasedPlanUseCase: SavePurchasedPlanUseCase,
    private val getPurchasedPlanUseCase: GetPurchasedPlanUseCase,
    private val setPendingSubscriptionValidationUseCase: SetPendingSubscriptionValidationUseCase,
    private val getPendingSubscriptionValidationUseCase: GetPendingSubscriptionValidationUseCase
) : ViewModel() {
    var googlePlayPlansMap: Map<String, SkuDetails> = emptyMap()
    var planUnderPurchase: PlanModel? = null

    private val _userStatusViewState = MutableLiveData<UserStatusViewState>()
    val userStatusViewState: LiveData<UserStatusViewState>
        get() = _userStatusViewState

    private val _plansViewState = MutableLiveData<PlansViewState>()
    val plansViewState: LiveData<PlansViewState>
        get() = _plansViewState

    private val _purchaseViewState = MutableLiveData<ViewState<Unit, BaseErrorData<BaseErrorStatus>>>()
    val purchaseViewState: LiveData<ViewState<Unit, BaseErrorData<BaseErrorStatus>>>
        get() = _purchaseViewState

    private val tag = this.javaClass.simpleName
    private val googlePlaySubsPlansCode = listOf(PlanCode.ANNUAL, PlanCode.MONTHLY)

    fun fetchUserStatus() {
        val pendingSubscriptionValidationResult = getPendingSubscriptionValidationUseCase.runSync()

        pendingSubscriptionValidationResult.success?.let {
            _userStatusViewState.postValue(UserStatusViewState.FreeUser)
        } ?: run {
            val purchasedPlan = getPurchasedPlanUseCase.runSync()

            purchasedPlan.unwrap(
                successBlock = {
                    if (it.endDate.toDate(SupportedDateFormat.SERVER).before(Date())) {
                        removeSubscription()
                    } else {
                        _userStatusViewState.postValue(UserStatusViewState.PremiumUser(it))
                    }
                },
                errorBlock = {
                    _userStatusViewState.postValue(UserStatusViewState.FreeUser)

                    when {
                        it.errorBody != BaseErrorStatus.DATA_NOT_FOUND -> Log.e(
                            tag,
                            "Error getting saved purchase plan: $it"
                        )
                    }
                }
            )
        }
    }

    fun removeSubscription() {
        savePurchasedPlanUseCase.runSync(null)

        _userStatusViewState.postValue(UserStatusViewState.FreeUser)
    }

    fun loadActivePlans() {
        _plansViewState.postValue(PlansViewState.Loading)
        Log.d(tag, "Loading plans")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "Preparing loading google play plans")
            val googlePlayPlans = loadGooglePlaySubsPlans()
            Log.d(tag, "Loaded ${googlePlayPlans.size} google play plans")

            val plansResult = getPlansUseCase.runAsync()

            plansResult.success?.let {
                val mergedPlans = mergePlans(it, googlePlayPlans)

                verifyPendingSubscriptionValidation(mergedPlans)
            } ?: run {
                _plansViewState.postValue(PlansViewState.Error(plansResult.error!!))
            }
        }
    }

    fun onGooglePlayPlanPurchaseSuccess(purchasedPlan: GooglePlayPurchasedPlanModel, forceError: Boolean) {
        _purchaseViewState.postValue(ViewState.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            validatePurchasedStorePlan(purchasedPlan, forceError, this)
        }
    }

    //region VALIDATE PENDING ACKNOWLEDGE PURCHASE
    fun validatePendingAcknowledgePurchase(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) {
        val googlePlayPurchasedPlansResult =
            googlePlayBillingClientWrapper.querySyncSubscriptionPurchases()

        googlePlayPurchasedPlansResult.success?.let {
            validatePurchasedPlans(it, pendingSubscriptionValidationModel.storeId)
        } ?: run {
            val error = BaseErrorData(
                errorBody = BaseErrorStatus.DEFAULT_ERROR,
                errorMessage = "Não foi possível buscar suas compras na GooglePlay"
            )
            _purchaseViewState.postValue(ViewState.Error(error))
        }
    }

    private fun validatePurchasedPlans(purchasedPlans: List<GooglePlayPurchasedPlanModel>, storeId: String) {
        val googlePlayPurchasedPlan = purchasedPlans.firstOrNull { x -> x.productId == storeId }

        googlePlayPurchasedPlan?.let {
            _purchaseViewState.postValue(ViewState.Loading())

            viewModelScope.launch(Dispatchers.IO) {
                acknowledgePurchasedPlan(it)
            }
        } ?: run {
            val error = BaseErrorData(
                errorBody = BaseErrorStatus.DEFAULT_ERROR,
                errorMessage = "Não foi possível encontrar sua compra na GooglePlay."
            )
            _purchaseViewState.postValue(ViewState.Error(error))
        }
    }

    private suspend fun acknowledgePurchasedPlan(purchasedPlan: GooglePlayPurchasedPlanModel) {
        val acknowledgePurchaseResult = acknowledgePurchase(purchasedPlan)
        acknowledgePurchaseResult.unwrap(
            successBlock = {
                setPendingSubscriptionValidationUseCase.runSync(null)

                _purchaseViewState.postValue(ViewState.Success(Unit))
            },
            errorBlock = {
                val error = BaseErrorData(
                    errorBody = BaseErrorStatus.DEFAULT_ERROR,
                    errorMessage = "Não foi possível reconhecer sua compra na GooglePlay."
                )
                _purchaseViewState.postValue(ViewState.Error(error))
            }
        )
    }
    //endregion


    fun validatePendingServerSyncSubscription(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) {
        val googlePlayPurchasedPlansResult =
            getGooglePlayPurchasedPlans(pendingSubscriptionValidationModel)

        googlePlayPurchasedPlansResult.unwrap(
            successBlock = {
                _purchaseViewState.postValue(ViewState.Loading())

                viewModelScope.launch(Dispatchers.IO) {
                    validatePurchasedStorePlan(it, false, this)
                }
            },
            errorBlock = {
                _purchaseViewState.postValue(ViewState.Error(it))
            }
        )
    }

    //region VALIDATE PURCHASED STORE PLAN
    private fun validatePurchasedStorePlan(googlePlayPurchasedPlan: GooglePlayPurchasedPlanModel, forceError: Boolean, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            val validationParams = ValidatePurchasedStorePlanUseCase.Params(
                googlePlayPurchasedPlan,
                forceError
            )
            val validationResult = validatePurchasedStorePlanUseCase.runAsync(validationParams)

            var pendingSubscriptionValidationModel: PendingSubscriptionValidationModel? = null

            validationResult.success?.let { purchasedPlan ->
                savePurchasedPlanUseCase.runSync(purchasedPlan)
                pendingSubscriptionValidationModel = acknowledgePurchasedStorePlan(googlePlayPurchasedPlan)
            } ?: run {
                pendingSubscriptionValidationModel = PendingSubscriptionValidationModel(
                    PendingSubscriptionValidationType.BACKEND,
                    googlePlayPurchasedPlan.productId,
                    Date().toSupportedDateFormat(SupportedDateFormat.SERVER)
                )

                _purchaseViewState.postValue(ViewState.Error(validationResult.error))
            }

            setPendingSubscriptionValidationUseCase.runSync(pendingSubscriptionValidationModel)
        }
    }

    private suspend fun acknowledgePurchasedStorePlan(purchasedPlan: GooglePlayPurchasedPlanModel): PendingSubscriptionValidationModel? {
        val acknowledgePurchaseResult = acknowledgePurchase(purchasedPlan)
        when {
            acknowledgePurchaseResult.success != null -> {
                _purchaseViewState.postValue(ViewState.Success(Unit))
                return null
            }
            else -> {
                _purchaseViewState.postValue(ViewState.Error(
                    BaseErrorData(
                        errorBody = BaseErrorStatus.DEFAULT_ERROR,
                        errorMessage = "Não foi possível reconhecer a compra na Google Play"
                    )
                ))

                return PendingSubscriptionValidationModel(
                    PendingSubscriptionValidationType.GOOGLE_PLAY_ACKNOWLEDGE,
                    purchasedPlan.productId,
                    Date().toSupportedDateFormat(SupportedDateFormat.SERVER)
                )

            }
        }
    }
    //endregion

    private fun getGooglePlayPurchasedPlans(
        pendingSubscriptionValidationModel: PendingSubscriptionValidationModel
    ): ResultWrapper<GooglePlayPurchasedPlanModel, BaseErrorData<BaseErrorStatus>> {
        val googlePlayPurchasedPlansResult = googlePlayBillingClientWrapper.querySyncSubscriptionPurchases()

        googlePlayPurchasedPlansResult.success?.let {
            val googlePlayPurchasedPlan =
                it.firstOrNull { x -> x.productId == pendingSubscriptionValidationModel.storeId }
            return if (googlePlayPurchasedPlan != null) {
                ResultWrapper(googlePlayPurchasedPlan)
            } else {
                ResultWrapper(
                    error = BaseErrorData(
                        errorBody = BaseErrorStatus.DEFAULT_ERROR,
                        errorMessage = "Não foi possível encontrar sua compra na GooglePlay."
                    )
                )
            }
        } ?: run {
            return ResultWrapper(
                error = BaseErrorData(
                    errorBody = BaseErrorStatus.DEFAULT_ERROR,
                    errorMessage = "Não foi possível buscar suas compras na GooglePlay"
                )
            )
        }
    }

    private suspend fun acknowledgePurchase(purchasedPlan: GooglePlayPurchasedPlanModel): ResultWrapper<Unit, GooglePlayBillingResponseCodeModel> {
        return googlePlayBillingClientWrapper.acknowledgePurchase(
            purchasedPlan.purchaseToken,
            "USER_ID"
        )
    }

    private fun verifyPendingSubscriptionValidation(mergedPlans: List<PlanModel>) {
        val pendingSubscriptionValidation = getPendingSubscriptionValidationUseCase.runSync()

        pendingSubscriptionValidation.unwrap(
            successBlock = { pendingSubscriptionValidationModel ->
                val pendingPlan =
                    mergedPlans.firstOrNull { p -> p.storeId == pendingSubscriptionValidationModel.storeId }
                when {
                    pendingPlan != null -> _plansViewState.postValue(
                        PlansViewState.PendingValidation(
                            pendingSubscriptionValidationModel
                        )
                    )

                    else -> _plansViewState.postValue(PlansViewState.Success(mergedPlans))
                }
            },
            errorBlock = {
                _plansViewState.postValue(PlansViewState.Success(mergedPlans))
            }
        )
    }

    private fun mergePlans(serverPlans: List<PlanModel>, googlePlayPlans: List<PlanModel>): List<PlanModel> {
        val mergedPlans = arrayListOf<PlanModel>()

        var mergedGooglePlayPlansCount = 0

        serverPlans.forEach { serverPlan ->
            if (serverPlan.gateway == PlanGateway.GOOGLE_PLAY) {
                val validGooglePlayPlan =
                    googlePlayPlans.firstOrNull { googlePlanPlan -> googlePlanPlan.storeId == serverPlan.storeId }

                if (validGooglePlayPlan != null) {
                    mergedGooglePlayPlansCount++
                    mergedPlans.add(serverPlan)
                } else {
                    Log.w(tag, "Invalid google play plan from server: ${serverPlan.storeId}")
                }
            } else mergedPlans.add(serverPlan)
        }

        if (mergedGooglePlayPlansCount == 0) {
            Log.w(tag, "There is no valid google play plan to show")
        }

        return mergedPlans
    }

    private suspend fun loadGooglePlaySubsPlans(): List<PlanModel> {
        val googlePlayPlans = arrayListOf<PlanModel>()

        val googlePlayPlansResponse = googlePlayBillingClientWrapper.querySkuDetails(
            plansCodes = googlePlaySubsPlansCode,
            type = GooglePlayBillingType.SUBS
        )

        Log.d(tag, "googlePlayPlansResponse: $googlePlayPlansResponse")

        googlePlayPlansResponse.success?.let {
            googlePlayPlansMap = it

            Log.d(tag, "googlePlayPlansResponse mapping")
            googlePlayPlansMap.map { skuMap ->
                val plan = skuMap.value.toPlanModel()
                googlePlayPlans.add(plan)
            }
            Log.i(tag, "${googlePlayPlans.size} google play plans added")
        } ?: run {
            Log.w(tag, "Error loading google play plans: ${googlePlayPlansResponse.error}")
        }

        return googlePlayPlans
    }
}