package br.com.pebmed.domain.base.usecase

/**
 * @Descrição: Classe base para casos de uso que não necessitam de parâmetros para serem executados
 */
abstract class NoParamsBaseUseCase<RESULT>: UseCase<RESULT, NoParams> {
    override suspend fun runAsync(): RESULT {
        throw InvalidUseCaseCall("If you want to run without params and asynchronously, you should extend NoParamsBaseAsyncUseCase")
    }

    override suspend fun runAsync(params: NoParams): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and asynchronously, you should extend BaseAsyncUseCase")
    }

    override fun runSync(params: NoParams): RESULT {
        throw InvalidUseCaseCall("If you want to run with params and synchronously, you should extend BaseUseCase")
    }
}