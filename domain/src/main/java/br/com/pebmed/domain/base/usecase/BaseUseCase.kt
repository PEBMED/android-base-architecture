package br.com.pebmed.domain.base.usecase

abstract class BaseUseCase<RESULT, PARAMS>: UseCase<RESULT, PARAMS> {
    override suspend fun runAsync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and asynchronously, you should extend NoParamsBaseAsyncUseCase")
    }

    override fun runSync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and synchronously, you should extend NoParamsBaseUseCase")
    }

    override suspend fun runAsync(params: PARAMS): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and asynchronously, you should extend BaseAsyncUseCase")
    }
}