package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.NoParamsBaseUseCase
import br.com.pebmed.domain.entities.billing.PendingSubscriptionValidationModel
import br.com.pebmed.domain.repository.BillingRepository

class GetPendingSubscriptionValidationUseCase(
    private val billingRepository: BillingRepository
) : NoParamsBaseUseCase<ResultWrapper<PendingSubscriptionValidationModel, BaseErrorData<BaseErrorStatus>>>() {
    override fun runSync(): ResultWrapper<PendingSubscriptionValidationModel, BaseErrorData<BaseErrorStatus>> {
        return billingRepository.getPendingSubscriptionValidation()
    }
}