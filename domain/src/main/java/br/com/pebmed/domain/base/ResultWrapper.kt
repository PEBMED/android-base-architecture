package br.com.pebmed.domain.base

sealed class ResultWrapper<out SUCCESS, out ERROR>(
    var keyValueMap: MutableMap<String, String>? = null,
    var statusCode: StatusType? = StatusType.DEFAULT_EXCEPTION
) {
    class Success<out SUCCESS>(
        val data: SUCCESS,
        keyValueMap: MutableMap<String, String>? = null,
        statusCode: StatusType? = StatusType.OK
    ) : ResultWrapper<SUCCESS, Nothing>(keyValueMap, statusCode)

    class Error<ERROR>(
        val error: ERROR? = null,
        keyValueMap: MutableMap<String, String>? = null,
        statusCode: StatusType? = StatusType.DEFAULT_EXCEPTION
    ) : ResultWrapper<Nothing, ERROR>(keyValueMap = keyValueMap, statusCode = statusCode)

    fun getValue(key: String): String? {
        var result: String? = null

        if (keyValueMap != null) {
            result = keyValueMap!![key]
        }

        return result
    }


    fun <NEW_SUCCESS, NEW_ERROR> transform(
        mapperSuccessFunction: (obj: SUCCESS) -> NEW_SUCCESS,
        mapperErrorFunction: (obj: ERROR?) -> NEW_ERROR
    ): ResultWrapper<NEW_SUCCESS, NEW_ERROR> {
        return when (this) {
            is Success -> {
                val newSuccess = mapperSuccessFunction.invoke(this.data)
                Success(
                    data = newSuccess,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }

            is Error -> {
                val newError = mapperErrorFunction.invoke(this.error)
                Error(
                    error = newError,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }
        }
    }

    fun <NEW_SUCCESS> transformSuccess(
        mapperFunction: (obj: SUCCESS) -> NEW_SUCCESS
    ): ResultWrapper<NEW_SUCCESS, ERROR> {
        return when (this) {
            is Success -> {
                val newSuccess = mapperFunction.invoke(this.data)
                Success(
                    data = newSuccess,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }

            is Error -> {
                Error(
                    error = this.error,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }
        }
    }

    fun <NEW_ERROR> transformError(
        mapperFunction: (obj: ERROR?) -> NEW_ERROR
    ): ResultWrapper<SUCCESS, NEW_ERROR> {
        return when (this) {
            is Success -> {
                Success(
                    data = this.data,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }

            is Error -> {
                val newError = mapperFunction.invoke(this.error)
                Error(
                    error = newError,
                    keyValueMap = this.keyValueMap,
                    statusCode = this.statusCode
                )
            }
        }
    }
}