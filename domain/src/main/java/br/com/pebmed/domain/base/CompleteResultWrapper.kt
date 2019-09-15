package br.com.pebmed.domain.base

class CompleteResultWrapper<SUCCESS, ERROR>(
    success: SUCCESS? = null,
    error: ERROR? = null,
    val keyValueMap: MutableMap<String, String>? = null,
    val statusCode: StatusType = StatusType.DEFAULT_EXCEPTION
) : ResultWrapper<SUCCESS, ERROR>(success, error) {

    fun getValue(key: String): String? {
        var result: String? = null

        if (keyValueMap != null) {
            result = keyValueMap[key]
        }

        return result
    }

    override fun <NEW_SUCCESS, NEW_ERROR> transform(
        mapperSuccessFunction: (obj: SUCCESS) -> NEW_SUCCESS,
        mapperErrorFunction: (obj: ERROR?) -> NEW_ERROR
    ): CompleteResultWrapper<NEW_SUCCESS, NEW_ERROR> {

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

    override fun <NEW_SUCCESS> transformSuccess(
        mapperFunction: (obj: SUCCESS) -> NEW_SUCCESS
    ): CompleteResultWrapper<NEW_SUCCESS, ERROR> {
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

    override fun <NEW_ERROR> transformError(
        mapperFunction: (obj: ERROR?) -> NEW_ERROR
    ): CompleteResultWrapper<SUCCESS, NEW_ERROR> {
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
