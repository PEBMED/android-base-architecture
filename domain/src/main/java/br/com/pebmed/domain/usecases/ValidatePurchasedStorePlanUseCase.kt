package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.BaseAsyncUseCase
import br.com.pebmed.domain.entities.billing.GooglePlayPurchasedPlanModel
import br.com.pebmed.domain.entities.billing.PurchasedPlanModel
import br.com.pebmed.domain.repository.BillingRepository

class ValidatePurchasedStorePlanUseCase(
    private val billingRepository: BillingRepository
) : BaseAsyncUseCase<ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>>, ValidatePurchasedStorePlanUseCase.Params>() {
    override suspend fun runAsync(params: Params): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>> {
        return billingRepository.validatePurchasedStorePlan(
            params.googlePlayPurchasedPlanModel,
            params.forceError
        )
    }

    data class Params(
        val googlePlayPurchasedPlanModel: GooglePlayPurchasedPlanModel,
        val forceError: Boolean = false
    )
}