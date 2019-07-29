package com.example.basearch.data

import com.example.basearch.data.remote.StatusType

sealed class ResultWrapper<SUCCESS, ERROR>(
    var keyValueMap: MutableMap<String, String>? = null,
    var statusCode: StatusType? = StatusType.DEFAULT_EXCEPTION
) {

    class Success<SUCCESS, ERROR>(
        val data: SUCCESS,
        keyValueMap: MutableMap<String, String>? = null,
        statusCode: StatusType? = StatusType.OK
    ) : ResultWrapper<SUCCESS, ERROR>(keyValueMap, statusCode)

    class Error<SUCCESS, ERROR>(
        val data: ERROR? = null,
        keyValueMap: MutableMap<String, String>? = null,
        statusCode: StatusType? = StatusType.DEFAULT_EXCEPTION
    ) : ResultWrapper<SUCCESS, ERROR>(keyValueMap = keyValueMap, statusCode = statusCode) {
        fun <NEW_SUCCESS> transformError(): ResultWrapper<NEW_SUCCESS, ERROR> {
            return Error(
                this.data,
                this.keyValueMap,
                this.statusCode
            )
        }
    }

    fun getValue(key: String): String? {
        var result: String? = null

        if (keyValueMap != null) {
            result = keyValueMap!![key]
        }

        return result
    }
}