package br.com.pebmed.domain.base

/**
 * @Descrição: Classe base para casos de uso que não necessitam de parâmetros para serem executados
 */
abstract class NoParamsBaseUseCase <RESULT>: IUseCase<RESULT, NoParamsDomain> {

    /**
     * Implementa a função run com parâmetros para que todos os casos de uso
     *  que extendam de NoParamsBaseUseCase não tenham a necessidade de
     *  implementar esse método.
     *
     * NOTA: Caso você esteja sobrescrevendo esse método, repense essa ação. Se houver necessidade
     * de utilizar parâmetros, seu caso de uso deve extender BaseUseCase
     */
    override suspend fun run(params: NoParamsDomain): RESULT = run()
}

/**
 * @Descricão: Classe que repesenta o "nada" para o caso de uso
 */
class NoParamsDomain