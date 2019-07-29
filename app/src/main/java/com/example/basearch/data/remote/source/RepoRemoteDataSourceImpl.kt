package com.example.basearch.data.remote.source

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.remote.BaseErrorData
import com.example.basearch.data.remote.api.RepoApi
import com.example.basearch.data.remote.model.response.GetReposResponse
import com.example.basearch.data.repository.BaseDataSourceImpl
import com.example.basearch.data.repository.ExecuteApiAsync
import retrofit2.Response

class RepoRemoteDataSourceImpl(private val repoApi: RepoApi) : RepoRemoteDataSouce, BaseDataSourceImpl() {
    override suspend fun getRepos(page: Int, language: String): ResultWrapper<GetReposResponse, BaseErrorData<Void>> {
        return safeApiCall(object : ExecuteApiAsync<GetReposResponse> {
            override suspend fun execute(): Response<GetReposResponse> {
                return repoApi.getRepos(page, language)
            }
        })
    }
}