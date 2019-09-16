package br.com.pebmed.domain.base

open class ResultWrapper<SUCCESS, ERROR>(
    val success: SUCCESS? = null,
    val error: ERROR? = null
) {

    fun isSuccess() : Boolean {
        return success != null
    }

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

