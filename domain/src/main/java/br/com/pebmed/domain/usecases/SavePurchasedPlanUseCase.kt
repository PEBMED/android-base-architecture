package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.usecase.BaseUseCase
import br.com.pebmed.domain.entities.billing.PurchasedPlanModel
import br.com.pebmed.domain.repository.BillingRepository

class SavePurchasedPlanUseCase(
    private val billingRepository: BillingRepository
) : BaseUseCase<Unit, PurchasedPlanModel?>() {
    override fun runSync(params: PurchasedPlanModel?) {
        billingRepository.savePurchasedPlan(params)
    }
}