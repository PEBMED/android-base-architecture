package br.com.pebmed.domain.entities.billing

data class PendingSubscriptionValidationModel(
    val type: PendingSubscriptionValidationType,
    val storeId: String,
    val date: String
)

enum class PendingSubscriptionValidationType {
    GOOGLE_PLAY_ACKNOWLEDGE,
    BACKEND
}