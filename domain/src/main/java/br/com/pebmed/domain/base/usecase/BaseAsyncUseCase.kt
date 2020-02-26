package br.com.pebmed.domain.base.usecase

/**
 * @Descrição: Classe base para a criação de casos de uso.
 */
abstract class BaseAsyncUseCase<RESULT, PARAMS>: UseCase<RESULT, PARAMS> {
    override suspend fun runAsync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and asynchronously, you should extend NoParamsBaseAsyncUseCase")
    }

    override fun runSync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and synchronously, you should extend NoParamsBaseUseCase")
    }

    override fun runSync(params: PARAMS): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and synchronously, you should extend BaseUseCase")
    }
}