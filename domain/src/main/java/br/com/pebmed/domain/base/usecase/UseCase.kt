package br.com.pebmed.domain.base.usecase

interface UseCase<RESULT, PARAMS> {
    /**
     * Função utilizada para executar um caso de uso de forma assincrona quando não há necessidade de parâmetro.
     */
    suspend fun runAsync(): RESULT

    /**
     * Função utilizada para executar um caso de uso de forma sincrona quando não há necessidade de parâmetro.
     */
    fun runSync(): RESULT

    /**
     * Função utilizada para executar um caso de uso de forma assincrona quando há necessidade de parâmetro.
     *
     * @param params modelo com os parâmetros esperados
     */
    suspend fun runAsync(params: PARAMS): RESULT

    /**
     * Função utilizada para executar um caso de uso de forma sincrona quando há necessidade de parâmetro.
     *
     * @param params modelo com os parâmetros esperados
     */
    fun runSync(params: PARAMS): RESULT
}