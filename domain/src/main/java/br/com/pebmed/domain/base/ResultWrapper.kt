package br.com.pebmed.domain.base

/**
 * @Descrição: ResultWrapper é uma classe que encapsula objetos de erro e sucesso
 *  para que a comunicação entre camadas ocorra de forma mais padronizada
 */
open class ResultWrapper<SUCCESS, ERROR>(
    val success: SUCCESS? = null,
    val error: ERROR? = null
) {

    /**
     * Mapeia o modelo de sucesso e erro atual para o modelo de sucesso e erro dado
     *
     * @param mapperSuccessFunction função que realizará a conversão do sucesso
     * @param mapperErrorFunction função que realizará a conversão do erro
     * @param TO_SUCCESS modelo e sucesso a ser gerado como resultado
     * @param TO_ERROR modelo e erro a ser gerado como resultado
     *
     * @return ResultWrapper com o modelo de sucesso passado
     */
    open fun <TO_SUCCESS, TO_ERROR> transform(
        mapperSuccessFunction: (originalSuccess: SUCCESS) -> TO_SUCCESS,
        mapperErrorFunction: (originalError: ERROR?) -> TO_ERROR
    ): ResultWrapper<TO_SUCCESS, TO_ERROR> {
        return if (this.success != null) {
            ResultWrapper(
                success = mapperSuccessFunction.invoke(this.success)
            )
        } else {
            ResultWrapper(
                error = mapperErrorFunction.invoke(this.error)
            )
        }
    }

    /**
     * Mapeia o modelo de sucesso atual para o modelo de sucesso dado
     *
     * @param mapperFunction função que realizará a conversão do sucesso
     * @param TO_SUCCESS modelo e sucesso a ser gerado como resultado
     *
     * @return ResultWrapper com o modelo de sucesso passado
     */
    open fun <TO_SUCCESS> transformSuccess(
        mapperFunction: (originalSuccess: SUCCESS) -> TO_SUCCESS
    ): ResultWrapper<TO_SUCCESS, ERROR> {
        return if (this.success != null) {
            ResultWrapper(
                success = mapperFunction.invoke(this.success)
            )
        } else {
            ResultWrapper(
                error = this.error
            )
        }
    }

    /**
     * Mapeia o modelo de erro atual para o modelo de erro dado
     *
     * @param mapperFunction função que realizará a conversão do erro
     * @param TO_ERROR modelo e erro a ser gerado como resultado
     *
     * @return ResultWrapper com o modelo de erro passado
     */
    open fun <TO_ERROR> transformError(
        mapperFunction: (originalError: ERROR?) -> TO_ERROR
    ): ResultWrapper<SUCCESS, TO_ERROR> {
        return if (this.success != null) {
            ResultWrapper(
                success = this.success
            )
        } else {
            ResultWrapper(
                error = mapperFunction.invoke(this.error)
            )
        }
    }
}