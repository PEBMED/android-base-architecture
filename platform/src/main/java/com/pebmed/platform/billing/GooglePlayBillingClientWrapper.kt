package com.pebmed.platform.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PlanGateway
import br.com.pebmed.domain.entities.PlanModel
import br.com.pebmed.domain.entities.PurchasedPlanModel
import com.android.billingclient.api.*
import com.pebmed.platform.base.SingleLiveEvent
import kotlinx.coroutines.isActive
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GooglePlayBillingClientWrapper(
    private val context: Context
) : LifecycleObserver, PurchasesUpdatedListener, BillingClientStateListener {
    private val _connectionStatusEvent =
        SingleLiveEvent<GooglePlayBillingResponseCodeModel>()
    val connectionStatusEvent: LiveData<GooglePlayBillingResponseCodeModel>
        get() = _connectionStatusEvent

    private val _purchaseUpdateEvent =
        SingleLiveEvent<ResultWrapper<List<PurchasedPlanModel>, GooglePlayBillingResponseCodeModel>>()
    val purchaseUpdateEvent: LiveData<ResultWrapper<List<PurchasedPlanModel>, GooglePlayBillingResponseCodeModel>>
        get() = _purchaseUpdateEvent

    private val _priceChangeEvent =
        SingleLiveEvent<GooglePlayBillingResponseCodeModel>()
    val priceChangeEvent: LiveData<GooglePlayBillingResponseCodeModel>
        get() = _priceChangeEvent

    private val tag = "GooglePlayBillingClient"
    private lateinit var billingClient: BillingClient
    private var retryConnectionQty = 0
    private val maxReconnectionAttempts = 5

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        Log.i(tag, "ON_CREATE")

        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases() // Not used for subscriptions.
            .build()
        if (!billingClient.isReady) {
            Log.i(tag, "BillingClient: Start connection...")
            billingClient.startConnection(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.i(tag, "ON_DESTROY")
        if (billingClient.isReady) {
            Log.i(tag, "BillingClient can only be used once -- closing connection")

            endConnection()
        }
    }

    fun endConnection() = billingClient.endConnection()

    fun isReady() = billingClient.isReady

    suspend fun querySkuDetails(plansCodes: List<String>, type: GooglePlayBillingType): ResultWrapper<Map<String, String>, GooglePlayBillingResponseCodeModel> {
        return suspendCoroutine { continuation ->
            Log.i(tag, "querySkuDetails")
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setType(type.value)
                .setSkusList(plansCodes)
                .build()

            Log.i(tag, "querySkuDetailsAsync")
            billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                val debugMessage = billingResult.debugMessage

                when {
                    responseCode.isSuccess() -> {
                        Log.i(tag, "onSkuDetailsResponse: $responseCode - $debugMessage")
                        if (skuDetailsList == null) {
                            Log.i(tag, "onSkuDetailsResponse: null SkuDetails list")

                            continuation.safeResume(
                                ResultWrapper(
                                    success = emptyMap()
                                )
                            )
                        } else {
                            val hasMap = LinkedHashMap<String, String>().apply {
                                for (details in skuDetailsList) {
                                    put(details.sku, details.originalJson)
                                }
                            }
                            Log.i(tag, "onSkuDetailsResponse: count ${hasMap.size}")

                            val result =
                                ResultWrapper<Map<String, String>, GooglePlayBillingResponseCodeModel>(
                                    success = hasMap
                                )
                            continuation.safeResume(result)
                        }
                    }
                    else -> {
                        Log.i(tag, "onSkuDetailsResponse: $responseCode - $debugMessage")

                        continuation.safeResume(ResultWrapper(error = responseCode))
                    }
                }
            }
        }
    }

    fun querySyncSubscriptionPurchases(): ResultWrapper<List<Purchase>, String> {
        if (!billingClient.isReady) {
            val errorMessage = "queryPurchases: BillingClient is not ready"
            Log.e(tag, errorMessage)

            return ResultWrapper(error = errorMessage)
        }

        Log.i(tag, "queryPurchases: SUBS")
        val result = billingClient.queryPurchases(BillingClient.SkuType.SUBS)

        val purchasesList = result.purchasesList ?: emptyList()
        Log.i(tag, "queryPurchases result szie: ${purchasesList.size}")

        return ResultWrapper(success = purchasesList)
    }

    suspend fun queryAsyncSubscriptionPurchases(): ResultWrapper<List<PurchaseHistoryRecord>, String> {
        if (!billingClient.isReady) {
            val errorMessage = "queryPurchases: BillingClient is not ready"
            Log.e(tag, errorMessage)

            return ResultWrapper(error = errorMessage)
        }

        return suspendCoroutine { continuation ->
            Log.i(tag, "queryPurchases: SUBS")
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) { billingResult, purchaseHistoryRecordList ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                when {
                    responseCode.isSuccess() -> {
                        val purchasesList = purchaseHistoryRecordList ?: emptyList()
                        Log.i(tag, "queryPurchases result szie: ${purchasesList.size}")

                        continuation.safeResume(ResultWrapper(success = purchasesList))
                    }
                    else -> {
                        continuation.safeResume(ResultWrapper(error = responseCode.toString()))
                    }
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity, skuDetailsJson: String): Int {
        val skuDetails = SkuDetails(skuDetailsJson)
        val billingBuilder = BillingFlowParams.newBuilder().setSkuDetails(skuDetails)

        val billingParams = billingBuilder.build()

        val sku = billingParams.sku
        val oldSku = billingParams.oldSku
        Log.i(tag, "launchBillingFlow: sku: $sku, oldSku: $oldSku")

        if (!billingClient.isReady) {
            Log.i(tag, "launchBillingFlow: BillingClient is not ready")
        }

        val billingResult = billingClient.launchBillingFlow(activity, billingParams)

        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.i(tag, "launchBillingFlow: BillingResponse $responseCode - $debugMessage")

        return responseCode
    }

    suspend fun acknowledgePurchase(purchaseToken: String, userId: String): ResultWrapper<Unit, GooglePlayBillingResponseCodeModel> {
        return suspendCoroutine { continuation ->
            Log.i(tag, "acknowledgePurchase")
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .setDeveloperPayload(userId)
                .build()

            billingClient.acknowledgePurchase(params) { billingResult ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                val debugMessage = billingResult.debugMessage
                Log.i(tag, "acknowledgePurchase: $responseCode - $debugMessage")

                when {
                    responseCode.isSuccess() -> {
                        continuation.safeResume(ResultWrapper(success = Unit))
                    }
                    else -> continuation.safeResume(ResultWrapper(error = responseCode))
                }
            }
        }
    }

    suspend fun getFirstSkuDetails(plansCodes: List<String>, type: GooglePlayBillingType): ResultWrapper<String, GooglePlayBillingResponseCodeModel> {
        val plans = querySkuDetails(plansCodes, type)

        return if (plans.success?.isNotEmpty() == true) {
            val firstSkuDetails = plans.success!!.entries.toList().first().value

            ResultWrapper(success = firstSkuDetails)
        } else {
            ResultWrapper(error = GooglePlayBillingResponseCodeModel.ERROR)
        }
    }

    fun launchPriceChangeConfirmationFlow(activity: Activity, skuDetailsJson: String) {
        val skuDetails = SkuDetails(skuDetailsJson)
        val priceChangeFlowParams = PriceChangeFlowParams.newBuilder().setSkuDetails(skuDetails).build()

        billingClient.launchPriceChangeConfirmationFlow(activity, priceChangeFlowParams) { billingResult ->
            val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
            val debugMessage = billingResult.debugMessage
            Log.i(tag, "launchPriceChangeConfirmationFlow: $responseCode - $debugMessage")

            _priceChangeEvent.postValue(responseCode)
        }

    }

    fun mapSkuDetailsToPlanModel(skuDetailsJson: String) : PlanModel {
        val skuDetails = SkuDetails(skuDetailsJson)

        return PlanModel(
            id = "-1",
            storeId = skuDetails.sku,
            title = skuDetails.title,
            price = skuDetails.price,
            gateway = PlanGateway.GOOGLE_PLAY,
            customMessage = ""
        )
    }

    fun mapPurchaseToPurchasedPlanModel(purchase: Purchase) : PurchasedPlanModel {
        return PurchasedPlanModel(
            orderId = purchase.orderId,
            productId = purchase.sku,
            purchaseToken = purchase.purchaseToken
        )
    }

    //region BILLING CLIENT STATE LISTENER
    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
        val debugMessage = billingResult.debugMessage
        Log.i(tag, "onBillingSetupFinished: $responseCode - $debugMessage")

        _connectionStatusEvent.postValue(responseCode)
    }

    override fun onBillingServiceDisconnected() {
        Log.i(tag, "onBillingServiceDisconnected")

        if (retryConnectionQty < maxReconnectionAttempts) {
            Log.i(tag, "trying to reconnect - attempt: $retryConnectionQty")
            billingClient.startConnection(this)
        } else {
            Log.w(tag, "All attempts failed: $retryConnectionQty")
            val serviceDisconnected = BillingClient.BillingResponseCode.SERVICE_DISCONNECTED.toGooglePlayBillingResponseCodeModel()
            _connectionStatusEvent.postValue(serviceDisconnected)
        }
    }
    //endregion

    //region PURCHASES UPDATED LISTENER
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
        val debugMessage = billingResult.debugMessage
        Log.i(tag, "onPurchasesUpdated: $responseCode - $debugMessage")

        when {
            responseCode.isSuccess() -> {
                Log.i(tag, "processPurchases: ${purchases?.size} purchase(s)")

                _purchaseUpdateEvent.postValue(
                    ResultWrapper(
                        success = purchases?.map { mapPurchaseToPurchasedPlanModel(it) } ?: emptyList()
                    )
                )
            }
            else -> _purchaseUpdateEvent.postValue(ResultWrapper(error = responseCode))
        }
    }
    //endregion

    private fun Int.toGooglePlayBillingResponseCodeModel(): GooglePlayBillingResponseCodeModel {
        return when (this) {
            BillingClient.BillingResponseCode.OK -> GooglePlayBillingResponseCodeModel.OK
            BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> GooglePlayBillingResponseCodeModel.SERVICE_TIMEOUT
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> GooglePlayBillingResponseCodeModel.FEATURE_NOT_SUPPORTED
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> GooglePlayBillingResponseCodeModel.SERVICE_DISCONNECTED
            BillingClient.BillingResponseCode.USER_CANCELED -> GooglePlayBillingResponseCodeModel.USER_CANCELED
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> GooglePlayBillingResponseCodeModel.SERVICE_UNAVAILABLE
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> GooglePlayBillingResponseCodeModel.BILLING_UNAVAILABLE
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> GooglePlayBillingResponseCodeModel.ITEM_UNAVAILABLE
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> GooglePlayBillingResponseCodeModel.DEVELOPER_ERROR
            BillingClient.BillingResponseCode.ERROR -> GooglePlayBillingResponseCodeModel.ERROR
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> GooglePlayBillingResponseCodeModel.ITEM_ALREADY_OWNED
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> GooglePlayBillingResponseCodeModel.ITEM_NOT_OWNED
            else -> GooglePlayBillingResponseCodeModel.UNKNOWN
        }
    }
}

enum class GooglePlayBillingResponseCodeModel {
    OK,
    SERVICE_TIMEOUT,
    FEATURE_NOT_SUPPORTED,
    SERVICE_DISCONNECTED,
    USER_CANCELED,
    SERVICE_UNAVAILABLE,
    BILLING_UNAVAILABLE,
    ITEM_UNAVAILABLE,
    DEVELOPER_ERROR,
    ERROR,
    ITEM_ALREADY_OWNED,
    ITEM_NOT_OWNED,
    UNKNOWN
}

enum class GooglePlayBillingType(val value: String) {
    SUBS(BillingClient.SkuType.SUBS),
    INAPP(BillingClient.SkuType.INAPP)
}

fun GooglePlayBillingResponseCodeModel.isSuccess(): Boolean = when (this) {
    GooglePlayBillingResponseCodeModel.OK -> true
    GooglePlayBillingResponseCodeModel.SERVICE_TIMEOUT,
    GooglePlayBillingResponseCodeModel.FEATURE_NOT_SUPPORTED,
    GooglePlayBillingResponseCodeModel.SERVICE_DISCONNECTED,
    GooglePlayBillingResponseCodeModel.USER_CANCELED,
    GooglePlayBillingResponseCodeModel.SERVICE_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.BILLING_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.ITEM_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.DEVELOPER_ERROR,
    GooglePlayBillingResponseCodeModel.ERROR,
    GooglePlayBillingResponseCodeModel.ITEM_ALREADY_OWNED,
    GooglePlayBillingResponseCodeModel.ITEM_NOT_OWNED,
    GooglePlayBillingResponseCodeModel.UNKNOWN -> false
}

fun <T> Continuation<T>.safeResume(value: T) {
    if(this.context.isActive) {
        return this.resume(value)
    }
}