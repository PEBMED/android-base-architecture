package br.com.pebmed.data.base

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.StatusType
import br.com.pebmed.domain.base.CompleteResultWrapper
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseDataSourceImpl {
    inline fun <SUCCESS, reified ERROR> safeApiCall(executeApiAsync: () -> Response<SUCCESS>): CompleteResultWrapper<SUCCESS, BaseErrorData<ERROR>> {
        return try {
            val response = executeApiAsync.invoke()

            ApiResponseHandler.build(response)
        } catch (exception: Exception) {
            val baseErrorData = BaseErrorData<ERROR>(
                errorMessage = exception.message
            )

            val statusCode = when (exception) {
                is SocketTimeoutException -> {
                    StatusType.SOCKET_TIMEOUT_EXCEPTION
                }
                is UnknownHostException -> {
                    StatusType.UNKNOWN_HOST_EXCEPTION
                }
                is ConnectException -> {
                    StatusType.CONNECT_EXCEPTION
                }
                is NoRouteToHostException -> {
                    StatusType.NO_ROUTE_TO_HOST_EXCEPTION
                }
                is IOException -> {
                    StatusType.IO_EXCEPTION
                }
                else -> {
                    StatusType.DEFAULT_EXCEPTION
                }
            }

            CompleteResultWrapper(
                error = baseErrorData,
                statusCode = statusCode
            )
        }
    }

    inline fun <SUCCESS, reified ERROR> safeCall(executeAsync: () -> SUCCESS): ResultWrapper<SUCCESS, BaseErrorData<ERROR>> {
        return try {
            val response = executeAsync.invoke()
            CompleteResultWrapper(success = response)
        } catch (exception: Exception) {
            val baseErrorData =
                BaseErrorData<ERROR>(errorMessage = exception.message)
            CompleteResultWrapper(error = baseErrorData)
        }
    }
}
