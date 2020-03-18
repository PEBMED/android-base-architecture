package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.NoParamsBaseAsyncUseCase
import br.com.pebmed.domain.entities.billing.PlanModel
import br.com.pebmed.domain.repository.BillingRepository

class GetPlansUseCase(
    private val billingRepository: BillingRepository
) : NoParamsBaseAsyncUseCase<ResultWrapper<List<PlanModel>, BaseErrorData<BaseErrorStatus>>>() {
    override suspend fun runAsync(): ResultWrapper<List<PlanModel>, BaseErrorData<BaseErrorStatus>> {
        return ResultWrapper(success = billingRepository.getFakePlans())
    }
}