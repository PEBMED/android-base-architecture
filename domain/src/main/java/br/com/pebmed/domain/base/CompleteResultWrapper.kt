package br.com.pebmed.domain.base

/**
 * @Descrição: CompleteResultWrapper é uma classe filha de ResultWrapper que adiciona
 *  propriedades de uma requisição
 */
class CompleteResultWrapper<SUCCESS, ERROR>(
    success: SUCCESS? = null,
    error: ERROR? = null,
    val keyValueMap: MutableMap<String, String>? = null,
    val statusCode: StatusType = StatusType.DEFAULT_EXCEPTION
) : ResultWrapper<SUCCESS, ERROR>(success, error) {

    override fun <TO_SUCCESS, TO_ERROR> transform(
        mapperSuccessFunction: (originalSuccess: SUCCESS) -> TO_SUCCESS,
        mapperErrorFunction: (originalError: ERROR?) -> TO_ERROR
    ): CompleteResultWrapper<TO_SUCCESS, TO_ERROR> {

        return if (this.success != null) {
            CompleteResultWrapper(
                success = mapperSuccessFunction.invoke(this.success),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            CompleteResultWrapper(
                error = mapperErrorFunction.invoke(this.error),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }

    override fun <TO_SUCCESS> transformSuccess(
        mapperFunction: (originalSuccess: SUCCESS) -> TO_SUCCESS
    ): CompleteResultWrapper<TO_SUCCESS, ERROR> {
        return if (this.success != null) {
            CompleteResultWrapper(
                success = mapperFunction.invoke(this.success),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            CompleteResultWrapper(
                error = this.error,
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }

    override fun <TO_ERROR> transformError(
        mapperFunction: (originalError: ERROR?) -> TO_ERROR
    ): CompleteResultWrapper<SUCCESS, TO_ERROR> {
        return if (this.success != null) {
            CompleteResultWrapper(
                success = this.success,
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            CompleteResultWrapper(
                error = mapperFunction.invoke(this.error),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }
}
