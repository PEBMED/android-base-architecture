package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.NoParamsBaseUseCase
import br.com.pebmed.domain.entities.billing.PurchasedPlanModel
import br.com.pebmed.domain.repository.BillingRepository

class GetPurchasedPlanUseCase(
    private val billingRepository: BillingRepository
) : NoParamsBaseUseCase<ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>>>() {
    override fun runSync(): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>> {
        return billingRepository.getSavedPurchasedPlan()
    }
}