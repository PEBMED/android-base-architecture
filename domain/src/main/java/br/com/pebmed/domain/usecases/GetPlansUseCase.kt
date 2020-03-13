package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.NoParamsBaseAsyncUseCase
import br.com.pebmed.domain.entities.PlanModel
import kotlinx.coroutines.delay

class GetPlansUseCase : NoParamsBaseAsyncUseCase<ResultWrapper<List<PlanModel>, BaseErrorData<BaseErrorStatus>>>() {
    override suspend fun runAsync(): ResultWrapper<List<PlanModel>, BaseErrorData<BaseErrorStatus>> {
        delay(1000)

        val plans = getFakeServerData()

        return ResultWrapper(success = plans)
    }

    private fun getFakeServerData(): List<PlanModel> {
        val list = arrayListOf<PlanModel>()

//        list.add(
//            PlanModel(
//            id = 0,
//            storeId = "",
//        ))

        return list
    }
}