package br.com.pebmed.domain.entities.billing

data class PurchasedPlanModel(
    val plan: PlanModel,
    val active: Boolean,
    val endDate: String
)

data class GooglePlayPurchasedPlanModel(
    val orderId: String,
    val productId: String,
    val purchaseToken: String
)