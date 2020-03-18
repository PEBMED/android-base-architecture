package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.usecase.BaseUseCase
import br.com.pebmed.domain.entities.billing.PendingSubscriptionValidationModel
import br.com.pebmed.domain.repository.BillingRepository

class SetPendingSubscriptionValidationUseCase(
    private val billingRepository: BillingRepository
) : BaseUseCase<Unit, PendingSubscriptionValidationModel?>() {
    override fun runSync(params: PendingSubscriptionValidationModel?) {
        billingRepository.setPendingSubscriptionValidation(params)
    }
}