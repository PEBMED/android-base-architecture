package br.com.pebmed.data.base

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.StatusType
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.extensions.fromJsonGeneric
import com.google.gson.Gson
import retrofit2.Response

object ApiResponseHandler {
    inline fun <SUCCESS, reified ERROR> build(response: Response<SUCCESS>): CompleteResultWrapper<SUCCESS, BaseErrorData<ERROR>> {
        val headers = response.headers()

        val getHeadersHashMap = {
            val keyValueMap: MutableMap<String, String> = HashMap()

            headers.names().map { headerKey ->
                val headerValue = headers.get(headerKey)
                keyValueMap[headerKey] = headerValue ?: ""
            }

            keyValueMap
        }

        if (response.isSuccessful) {
            val body = response.body()
            return if (body != null)
                CompleteResultWrapper(
                    success = body,
                    keyValueMap = getHeadersHashMap(),
                    statusCode = StatusType.getByCode(response.code())
                )
            else
                CompleteResultWrapper(
                    keyValueMap = getHeadersHashMap(),
                    statusCode = StatusType.NULL_BODY_EXCEPTION
                )
        } else {
            var errorData: ERROR? = null

            if (ERROR::class != Unit::class) {
                val msg = response.errorBody()?.string()

                if (!msg.isNullOrEmpty()) {
                    errorData = Gson().fromJsonGeneric<ERROR>(msg)
                }
            }

            val remoteErrorData = BaseErrorData<ERROR>(
                errorData,
                response.message()
            )

            return CompleteResultWrapper(
                error = remoteErrorData,
                keyValueMap = getHeadersHashMap(),
                statusCode = StatusType.getByCode(response.code())
            )
        }
    }
}