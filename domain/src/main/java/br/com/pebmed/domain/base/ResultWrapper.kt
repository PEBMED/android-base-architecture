package br.com.pebmed.domain.base

open class ResultWrapper<SUCCESS, ERROR>(
    val success: SUCCESS? = null,
    val error: ERROR? = null
) {

    fun isSuccess() = success != null

    open fun <NEW_SUCCESS, NEW_ERROR> transform(
        mapperSuccessFunction: (obj: SUCCESS) -> NEW_SUCCESS,
        mapperErrorFunction: (obj: ERROR?) -> NEW_ERROR
    ): ResultWrapper<NEW_SUCCESS, NEW_ERROR> {
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

    open fun <NEW_SUCCESS> transformSuccess(
        mapperFunction: (obj: SUCCESS) -> NEW_SUCCESS
    ): ResultWrapper<NEW_SUCCESS, ERROR> {
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

    open fun <NEW_ERROR> transformError(
        mapperFunction: (obj: ERROR?) -> NEW_ERROR
    ): ResultWrapper<SUCCESS, NEW_ERROR> {
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

