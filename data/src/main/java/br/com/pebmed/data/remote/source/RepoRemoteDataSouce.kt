package br.com.pebmed.data.remote.source

import br.com.pebmed.data.remote.model.response.GetReposResponse
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GetReposErrorData

interface RepoRemoteDataSouce {
    suspend fun getRepos(
        page: Int,
        language: String
    ): ResultWrapper<GetReposResponse, BaseErrorData<GetReposErrorData>>
}