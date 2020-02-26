package br.com.pebmed.domain.base.usecase

abstract class NoParamsBaseAsyncUseCase<RESULT>: UseCase<RESULT, NoParams> {
    override fun runSync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and synchronously, you should extend NoParamsBaseUseCase")
    }

    override suspend fun runAsync(params: NoParams): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and asynchronously, you should extend BaseAsyncUseCase")
    }

    override fun runSync(params: NoParams): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and synchronously, you should extend BaseUseCase")
    }
}