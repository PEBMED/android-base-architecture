package br.com.pebmed.domain.entities

data class PurchasedPlanModel(
    val orderId: String,
    val productId: String,
    val purchaseToken: String
)