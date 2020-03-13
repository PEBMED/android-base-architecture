package br.com.pebmed.domain.entities

data class PlanModel(
    val id: String,
    val storeId: String,
    val title: String,
    val price: String,
    val gateway: PlanGateway,
    val customMessage: String
)

enum class PlanGateway {
    GOOGLE_PLAY,
    OTHER
}