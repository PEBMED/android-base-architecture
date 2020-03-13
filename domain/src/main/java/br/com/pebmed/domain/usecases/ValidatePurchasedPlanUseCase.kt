package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.BaseAsyncUseCase
import br.com.pebmed.domain.entities.PurchasedPlanModel
import kotlinx.coroutines.delay

class ValidatePurchasedPlanUseCase : BaseAsyncUseCase<ResultWrapper<Unit, BaseErrorData<BaseErrorStatus>>, PurchasedPlanModel>() {
    override suspend fun runAsync(params: PurchasedPlanModel): ResultWrapper<Unit, BaseErrorData<BaseErrorStatus>> {
        delay(1000)

        //Fake server response
        return ResultWrapper(success = Unit)
    }
}