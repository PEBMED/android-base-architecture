package com.pebmed.platform.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.billing.GooglePlayPurchasedPlanModel
import com.android.billingclient.api.*
import com.pebmed.platform.base.SingleLiveEvent
import kotlinx.coroutines.isActive
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GooglePlayBillingClientWrapper(
    private val context: Context
) : LifecycleObserver, PurchasesUpdatedListener {
    //region EVENTS
    /**
     * Event to notify when has some bolling connection status changes
     */
    private val _connectionStatusEvent = SingleLiveEvent<GooglePlayBillingResponseCodeModel>()
    val connectionStatusEvent: LiveData<GooglePlayBillingResponseCodeModel>
        get() = _connectionStatusEvent

    /**
     * Event to notify when has some purchase update
     */
    private val _purchaseUpdateEvent = SingleLiveEvent<ResultWrapper<List<GooglePlayPurchasedPlanModel>, GooglePlayBillingResponseCodeModel>>()
    val purchaseUpdateEvent: LiveData<ResultWrapper<List<GooglePlayPurchasedPlanModel>, GooglePlayBillingResponseCodeModel>>
        get() = _purchaseUpdateEvent

    /**
     * Event to notify when user finishes your iteration with price update
     */
    private val _priceChangeEvent = SingleLiveEvent<GooglePlayBillingResponseCodeModel>()
    val priceChangeEvent: LiveData<GooglePlayBillingResponseCodeModel>
        get() = _priceChangeEvent
    //endregion

    //region VARIABLES
    private var billingClient: BillingClient? = null
    private var retryConnectionQty = 0
    private val maxReconnectionAttempts = 5
    //endregion

    //region BILLING INITIALIZATION
    private fun initBillingClient() {
        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(context)
                .setListener(this@GooglePlayBillingClientWrapper)
                .enablePendingPurchases() // Not used for subscriptions.
                .build()
        }
    }

    private fun startBillingConnection(listener: BillingClientStateListener) {
        initBillingClient()
        if (!isReady()) {
            logInfo("BillingClient: Start connection...")
            logInfo("Previously on retryConnectionQty: $retryConnectionQty")
            retryConnectionQty = 0
            billingClient?.startConnection(listener)
        }
    }

    suspend fun startConnection(): GooglePlayBillingResponseCodeModel {
        logInfo("[startConnection] called")
        logInfo("[startConnection] BillingClient is ready: ${isReady()}")
        return if (isReady()) {
            _connectionStatusEvent.postValue(BillingClient.BillingResponseCode.OK.toGooglePlayBillingResponseCodeModel())
            GooglePlayBillingResponseCodeModel.OK
        } else {
            suspendCoroutine { continuation ->
                startConnectionWithContinuation(continuation)
            }
        }
    }

    private fun startConnectionWithContinuation(continuation: Continuation<GooglePlayBillingResponseCodeModel>) {
        try {
            startBillingConnection(createBillingClientStateListener(continuation))
        } catch (e: Exception) {
            logError(throwable = e)
            _connectionStatusEvent.postValue(BillingClient.BillingResponseCode.ERROR.toGooglePlayBillingResponseCodeModel())
            continuation.safeResume(BillingClient.BillingResponseCode.ERROR.toGooglePlayBillingResponseCodeModel())
        }
    }
    //endregion

    //region BILLING CLIENT STATE LISTENERS
    private fun createBillingClientStateListener(continuation: Continuation<GooglePlayBillingResponseCodeModel>): BillingClientStateListener {
        val tag = "startConnectionWithListener"
        return object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                val debugMessage = billingResult.debugMessage
                logInfo("[$tag] onBillingSetupFinished: $responseCode - $debugMessage")
                logInfo("[$tag] Is billing client ready: ${isReady()}")

                _connectionStatusEvent.postValue(responseCode)
                continuation.safeResume(responseCode)
            }

            override fun onBillingServiceDisconnected() {
                logInfo("onBillingServiceDisconnected")

                tryToReconnect(
                    retryBlock = {
                        startConnectionWithContinuation(continuation)
                    },
                    allAttemptsFailedBlock = {
                        val serviceDisconnected = BillingClient.BillingResponseCode.SERVICE_DISCONNECTED.toGooglePlayBillingResponseCodeModel()
                        _connectionStatusEvent.postValue(serviceDisconnected)
                        continuation.safeResume(serviceDisconnected)
                    }
                )
            }
        }
    }
    //endregion

    /**
     * Ends the connection. After finished, it's necessary to create a new BillingClient
     */
    fun endConnection() {
        if (isReady()) {
            logInfo("endConnection: Calling billingClient.endConnection().")
            billingClient?.endConnection()
        } else {
            logInfo("endConnection: No billingClient initialized or ready available.")
        }
        billingClient = null
    }

    /**
     * Verifies if billing lib is ready to be used
     */
    fun isReady() = billingClient?.isReady ?: false

    /**
     * Fetches [SkuDetails] for one or more items or subscriptions.
     */
    suspend fun querySkuDetails(plansCodes: List<String>, type: GooglePlayBillingType): ResultWrapper<Map<String, SkuDetails>, GooglePlayBillingResponseCodeModel> {
        return suspendCoroutine { continuation ->
            logInfo("querySkuDetails")
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setType(type.value)
                .setSkusList(plansCodes)
                .build()

            logInfo("querySkuDetailsAsync")
            billingClient?.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                val debugMessage = billingResult.debugMessage

                when {
                    responseCode.isSuccess() -> {
                        logInfo("onSkuDetailsResponse: $responseCode - $debugMessage")
                        if (skuDetailsList == null) {
                            logInfo("onSkuDetailsResponse: null SkuDetails list")

                            continuation.safeResume(ResultWrapper(success = emptyMap()))
                        } else {
                            val hasMap = LinkedHashMap<String, SkuDetails>().apply {
                                for (details in skuDetailsList) {
                                    put(details.sku, details)
                                }
                            }
                            logInfo("onSkuDetailsResponse: count ${hasMap.size}")

                            val result = ResultWrapper<Map<String, SkuDetails>, GooglePlayBillingResponseCodeModel>(success = hasMap)
                            continuation.safeResume(result)
                        }
                    }
                    else -> {
                        logInfo("onSkuDetailsResponse: $responseCode - $debugMessage")

                        logInfo("Is billing client ready: ${isReady()}")

                        continuation.safeResume(ResultWrapper(error = responseCode))
                    }
                }
            }
        }
    }

    /**
     * Searches synchronously for existing purchases at Google Play Billing
     */
    fun querySyncSubscriptionPurchases(): ResultWrapper<List<GooglePlayPurchasedPlanModel>, String> {
        if (!isReady()) {
            val errorMessage = "queryPurchases: BillingClient is not ready"
            logError(errorMessage)

            return ResultWrapper(error = errorMessage)
        }

        logInfo("queryPurchases: SUBS")
        val result = billingClient?.queryPurchases(BillingClient.SkuType.SUBS)

        val purchasesList = result?.purchasesList?.map { it.toDomainReceipt() } ?: emptyList()
        logInfo("queryPurchases result size: ${purchasesList.size}")

        return ResultWrapper(success = purchasesList)
    }

    /**
     * Searches asynchronously for existing purchases at Google Play Billing
     */
    suspend fun queryAsyncSubscriptionPurchases(): ResultWrapper<List<PurchaseHistoryRecord>, String> {
        if (!isReady()) {
            val errorMessage = "queryPurchases: BillingClient is not ready"
            logError(errorMessage)

            return ResultWrapper(error = errorMessage)
        }

        return suspendCoroutine { continuation ->
            logInfo("queryPurchases: SUBS")
            billingClient?.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS) { billingResult, purchaseHistoryRecordList ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                when {
                    responseCode.isSuccess() -> {
                        val purchasesList = purchaseHistoryRecordList ?: emptyList()
                        logInfo("queryPurchases result szie: ${purchasesList.size}")

                        continuation.safeResume(ResultWrapper(success = purchasesList))
                    }
                    else -> {
                        continuation.safeResume(ResultWrapper(error = responseCode.toString()))
                    }
                }
            }
        }
    }

    /**
     * Shows the purchase GooglePlay UI purchase
     */
    fun launchBillingFlow(activity: Activity, skuDetails: SkuDetails): Int? {
        val billingBuilder = BillingFlowParams.newBuilder().setSkuDetails(skuDetails)

        val billingParams = billingBuilder.build()

        val sku = billingParams.sku
        val oldSku = billingParams.oldSku
        logInfo("launchBillingFlow: sku: $sku, oldSku: $oldSku")

        if (!isReady()) {
            logInfo("launchBillingFlow: BillingClient is not ready")
        }

        val billingResult = billingClient?.launchBillingFlow(activity, billingParams)

        val responseCode = billingResult?.responseCode
        val debugMessage = billingResult?.debugMessage
        logInfo("launchBillingFlow: BillingResponse $responseCode - $debugMessage")

        return responseCode
    }

    /**
     * Purchases should be recognized after been associated to user.
     * Purchases that are not recognized on 3 days will be automatically reversed
     * This operation is now made at client, but could be made by server
     *
     * For more details, see:
     * https://developer.android.com/google/play/billing/billing_library_releases_notes#2_0_acknowledge
     */
    suspend fun acknowledgePurchase(purchaseToken: String, userId: String): ResultWrapper<Unit, GooglePlayBillingResponseCodeModel> {
        return suspendCoroutine { continuation ->
            logInfo("acknowledgePurchase")
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .setDeveloperPayload(userId)
                .build()

            billingClient?.acknowledgePurchase(params) { billingResult ->
                val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
                val debugMessage = billingResult.debugMessage
                logInfo("acknowledgePurchase: $responseCode - $debugMessage")

                when {
                    responseCode.isSuccess() -> {
                        continuation.safeResume(ResultWrapper(success = Unit))
                    }
                    else -> continuation.safeResume(ResultWrapper(error = responseCode))
                }
            }
        }
    }

    suspend fun getFirstSkuDetails(plansCodes: List<String>, type: GooglePlayBillingType): ResultWrapper<SkuDetails, GooglePlayBillingResponseCodeModel> {
        val plans = querySkuDetails(plansCodes, type)

        return if (plans.success?.isNotEmpty() == true) {
            val firstSkuDetails = plans.success!!.entries.toList().first().value

            ResultWrapper(success = firstSkuDetails)
        } else {
            ResultWrapper(error = GooglePlayBillingResponseCodeModel.ERROR)
        }
    }

    fun launchPriceChangeConfirmationFlow(activity: Activity, skuDetails: SkuDetails) {
        val priceChangeFlowParams = PriceChangeFlowParams.newBuilder().setSkuDetails(skuDetails).build()

        billingClient?.launchPriceChangeConfirmationFlow(activity, priceChangeFlowParams) { billingResult ->
            val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
            val debugMessage = billingResult.debugMessage
            logInfo("launchPriceChangeConfirmationFlow: $responseCode - $debugMessage")

            _priceChangeEvent.postValue(responseCode)
        }

    }

    //region PURCHASES UPDATED LISTENER
    /**
     * Purchases initialized by app or another source will be reported here.
     * ATTENTION! All purchased reported here must to be consumed or acknowledged.
     */
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        val responseCode = billingResult.responseCode.toGooglePlayBillingResponseCodeModel()
        val debugMessage = billingResult.debugMessage
        logInfo("onPurchasesUpdated: $responseCode - $debugMessage")

        when {
            responseCode.isSuccess() -> {
                logInfo("processPurchases: ${purchases?.size} purchase(s)")
                logPurchasesInfo(purchases)

                _purchaseUpdateEvent.postValue(ResultWrapper(success = purchases?.map { it.toDomainReceipt() } ?: emptyList()))
            }
            else -> _purchaseUpdateEvent.postValue(ResultWrapper(error = responseCode))
        }
    }

    private fun logPurchasesInfo(purchases: MutableList<Purchase>?) {
        purchases?.let { purchaseList ->
            purchaseList.forEach {
                logInfo("Purchase info: { \"orderId\": \"${it.orderId}\", \"purchaseTime\": ${it.purchaseTime}, \"purchaseState\": ${it.purchaseState}, \"autoRenewing\": ${it.isAutoRenewing}, \"acknowledged\": ${it.isAcknowledged} }")
            }
        } ?: logInfo("List of purchase is null")
    }

    //endregion

    private fun tryToReconnect(retryBlock: () -> Unit, allAttemptsFailedBlock: () -> Unit) {
        if (retryConnectionQty < maxReconnectionAttempts) {
            logInfo("trying to reconnect - attempt: $retryConnectionQty")
            retryBlock()
            retryConnectionQty++
        }
        else {
            logInfo("All attempts failed: $retryConnectionQty")
            allAttemptsFailedBlock()
            retryConnectionQty = 0
        }
    }

    private fun Int.toGooglePlayBillingResponseCodeModel(): GooglePlayBillingResponseCodeModel {
        return when(this) {
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

    private fun logInfo(message: String) {
        Log.i("GooglePlayBillingClient", message)
    }

    private fun logError(message: String? = null, throwable: Throwable? = null) {
        Log.e("GooglePlayBillingClient", message, throwable)
    }

    private fun <T> Continuation<T>.safeResume(value: T) {
        try {
            if (this.context.isActive) {
                this.resume(value)
            }
        } catch (illegalStateException: IllegalStateException) {
            logError("Unable to resume.", illegalStateException)
        }
    }
}