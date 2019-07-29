package com.example.basearch.data.repository

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.local.model.RepoCache
import com.example.basearch.data.remote.ApiResponseHandler
import com.example.basearch.data.remote.BaseErrorData
import com.example.basearch.data.remote.StatusType
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class BaseDataSourceImpl {
    suspend inline fun <SUCCESS, reified ERROR> safeApiCall(executeApiAsync: ExecuteApiAsync<SUCCESS>): ResultWrapper<SUCCESS, BaseErrorData<ERROR>> {
        return try {
            val response = executeApiAsync.execute()

            ApiResponseHandler().handleApiResponse(response)
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

            ResultWrapper.Error(baseErrorData, statusCode = statusCode)
        }
    }

    suspend inline fun<SUCCESS, reified ERROR> safeCall(executeAsync: ExecuteAsync<SUCCESS>): ResultWrapper<SUCCESS, BaseErrorData<ERROR>> {
        return try{
            val response = executeAsync.execute()
            ResultWrapper.Success(data = response)
        } catch (exception: Exception) {
            val baseErrorData = BaseErrorData<ERROR>(errorMessage = exception.message)
            ResultWrapper.Error(baseErrorData)
        }
    }
}

interface ExecuteApiAsync<T> {
    suspend fun execute(): Response<T>
}

interface ExecuteAsync<T> {
    suspend fun execute(): T
}
