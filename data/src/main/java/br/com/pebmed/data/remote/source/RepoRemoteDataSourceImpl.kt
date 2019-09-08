package br.com.pebmed.data.remote.source

import br.com.pebmed.data.remote.api.RepoApi
import br.com.pebmed.data.remote.model.response.GetReposResponse
import br.com.pebmed.data.repository.BaseDataSourceImpl
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GetReposErrorData

class RepoRemoteDataSourceImpl(private val repoApi: RepoApi) : RepoRemoteDataSouce,
    BaseDataSourceImpl() {
    override suspend fun getRepos(
        page: Int,
        language: String
    ): ResultWrapper<GetReposResponse, BaseErrorData<GetReposErrorData>> {
        return safeApiCall { repoApi.getRepos(page, language) }
    }
}