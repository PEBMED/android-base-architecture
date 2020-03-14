package com.pebmed.basearch.presentation.ui.billing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.PlanGateway
import br.com.pebmed.domain.entities.PlanModel
import br.com.pebmed.domain.entities.PurchasedPlanModel
import br.com.pebmed.domain.usecases.GetPlansUseCase
import br.com.pebmed.domain.usecases.ValidatePurchasedPlanUseCase
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import com.pebmed.platform.billing.GooglePlayBillingType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillingViewModel(
    private val googlePlayBillingClientWrapper: GooglePlayBillingClientWrapper,
    private val getPlansUseCase: GetPlansUseCase,
    private val validatePurchasedPlanUseCase: ValidatePurchasedPlanUseCase
) : ViewModel() {
    var googlePlayPlansMap: Map<String, String> = emptyMap()
    var planUnderPurchase: PlanModel? = null

    private val _plansViewState =
        MutableLiveData<ViewState<List<PlanModel>, BaseErrorData<BaseErrorStatus>>>()
    val plansViewState: LiveData<ViewState<List<PlanModel>, BaseErrorData<BaseErrorStatus>>>
        get() = _plansViewState

    private val _purchaseViewState =
        MutableLiveData<ViewState<Unit, BaseErrorData<BaseErrorStatus>>>()
    val purchaseViewState: LiveData<ViewState<Unit, BaseErrorData<BaseErrorStatus>>>
        get() = _purchaseViewState

    private val tag = this.javaClass.simpleName
    private val googlePlaySubsPlansCode = listOf("...")

    fun loadActivePlans() {
        _plansViewState.postValue(ViewState.Loading())

        Log.d(tag, "Loading plans")

        viewModelScope.launch(Dispatchers.IO) {
            val googlePlayPlans = loadGooglePlaySubsPlans()

            Log.d(tag, "Loaded ${googlePlayPlans.size} google play plans")

            val plansResult = getPlansUseCase.runAsync()

            plansResult.unwrap(
                successBlock = {
                    val mergedPlans = mergePlans(it, googlePlayPlans)

                    _plansViewState.postValue(ViewState.Success(mergedPlans))
                },
                errorBlock = {
                    _plansViewState.postValue(ViewState.Error(it))
                }
            )
        }
    }

    fun onGooglePlayPlanPurchaseSuccess(purchasedPlan: PurchasedPlanModel) {
        _purchaseViewState.postValue(ViewState.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            acknowledgePurchase(purchasedPlan, this)
        }
    }

    private fun mergePlans(
        serverPlans: List<PlanModel>,
        googlePlayPlans: List<PlanModel>
    ): List<PlanModel> {
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
        val googlePlayPlans = mutableListOf<PlanModel>()

        val googlePlayPlansResponse = googlePlayBillingClientWrapper.querySkuDetails(
            plansCodes = googlePlaySubsPlansCode,
            type = GooglePlayBillingType.SUBS
        )

        googlePlayPlansResponse.success?.let {
            googlePlayPlansMap = it

            googlePlayPlansMap.map { skuMap ->
                val plan = googlePlayBillingClientWrapper.mapSkuDetailsToPlanModel(skuMap.value)
                googlePlayPlans.add(plan)
            }
        } ?: run {
            Log.w(tag, "Error loading google play plans: ${googlePlayPlansResponse.error}")
        }

        return googlePlayPlans
    }

    private fun acknowledgePurchase(purchasedPlan: PurchasedPlanModel, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            val acknowledgePurchaseResult = googlePlayBillingClientWrapper.acknowledgePurchase(
                purchasedPlan.purchaseToken,
                "USER_ID"
            )

            if (acknowledgePurchaseResult.success != null) {
                validadePurchasedPlan(purchasedPlan, this)
            } else {
                val error = ViewState.Error<Unit, BaseErrorData<BaseErrorStatus>>(
                    BaseErrorData(
                        errorBody = BaseErrorStatus.DEFAULT_ERROR,
                        errorMessage = "Erro ao reconhecer compra: ${acknowledgePurchaseResult.error}"
                    )
                )
                _purchaseViewState.postValue(error)
            }
        }
    }

    private fun validadePurchasedPlan(purchasedPlan: PurchasedPlanModel, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            val validationResult = validatePurchasedPlanUseCase.runAsync(purchasedPlan)

            validationResult.unwrap(
                successBlock = {
                    _purchaseViewState.postValue(ViewState.Success(Unit))
                },
                errorBlock = {
                    _purchaseViewState.postValue(ViewState.Error(it))
                }
            )
        }
    }
}