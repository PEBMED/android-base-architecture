package br.com.pebmed.domain.base

class SuperResultWrapperV2<SUCCESS, ERROR>(
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
    ): SuperResultWrapperV2<NEW_SUCCESS, NEW_ERROR> {

        return if (this.success != null) {
            SuperResultWrapperV2(
                success = mapperSuccessFunction.invoke(this.success),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            SuperResultWrapperV2(
                error = mapperErrorFunction.invoke(this.error),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }

    override fun <NEW_SUCCESS> transformSuccess(
        mapperFunction: (obj: SUCCESS) -> NEW_SUCCESS
    ): SuperResultWrapperV2<NEW_SUCCESS, ERROR> {
        return if (this.success != null) {
            SuperResultWrapperV2(
                success = mapperFunction.invoke(this.success),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            SuperResultWrapperV2(
                error = this.error,
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }

    override fun <NEW_ERROR> transformError(
        mapperFunction: (obj: ERROR?) -> NEW_ERROR
    ): SuperResultWrapperV2<SUCCESS, NEW_ERROR> {
        return if (this.success != null) {
            SuperResultWrapperV2(
                success = this.success,
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        } else {
            SuperResultWrapperV2(
                error = mapperFunction.invoke(this.error),
                statusCode = this.statusCode,
                keyValueMap = this.keyValueMap
            )
        }
    }
}
