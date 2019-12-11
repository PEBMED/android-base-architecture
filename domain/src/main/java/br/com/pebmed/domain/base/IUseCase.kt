package br.com.pebmed.domain.base

interface IUseCase<RESULT, PARAMS> {

    /**
     * Método utilizado para executar o caso de uso quando não há necessidade de parâmetro.
     */
    suspend fun run(): RESULT

    /**
     * Método utilizado para executar o caso de uso quando há necessidade de parâmetro.
     *
     * @param params modelo com os parâmetros esperados
     */
    suspend fun run(params: PARAMS): RESULT

    /**
     * @WorkInProgress: devido à dificuldade de implementação de testes automatizados
     *  e algumas indefinições, o método invoke ainda não será utilizado
     *
     * Função acessível por quem instanciar o caso de uso.
     * Executa a função run de forma assíncrona e chama a função resultado
     * quando há uma resposta
     *
     * @param scope
     * @param params modelo com os parâmetros esperados
     * @param onResult função a ser executada quando houver um retorno
     */ /*
    operator fun invoke(
            scope: CoroutineScope,
            params: PARAMS? = null,
            onResult: (RESULT) -> Unit = {}
    ) {
        val backgroundJob = scope.async {
            if(params == null) run()
            else run(params)
        }
        scope.launch { onResult(backgroundJob.await()) }
    }
    */
}