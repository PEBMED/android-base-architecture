package br.com.pebmed.domain.base

/**
 * @Descrição: Classe base para a criação de casos de uso.
 */
abstract class BaseUseCase<RESULT, PARAMS>: IUseCase<RESULT, PARAMS> {

    /**
     * Implementa a função run sem parâmetros. Esta função NUNCA deve ser chamada. Caso não haja
     * necessidade de se passar parâmetros, considere extender NoParamsBaseUseCase
     *
     * NOTA: Caso você esteja sobrescrevendo esse método, repense essa ação. Se houver necessidade
     * de não utilizar parâmetros, seu caso de uso deve extender NoParamsBaseUseCase
     */
    override suspend fun run(): RESULT {
        throw InvalidMethodCall()
    }

}

class InvalidMethodCall: Exception("YOU SHALL NOT PASS!!! \nThis method could not be called. " +
        "If you want call the UseCase without params, you should extend NoParamsBaseUseCase")