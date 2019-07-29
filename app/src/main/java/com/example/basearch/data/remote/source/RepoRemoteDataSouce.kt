package com.example.basearch.data.remote.source

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.remote.BaseErrorData
import com.example.basearch.data.remote.model.response.GetReposResponse

interface RepoRemoteDataSouce {
    suspend fun getRepos(page: Int, language: String): ResultWrapper<GetReposResponse, BaseErrorData<Void>>
}