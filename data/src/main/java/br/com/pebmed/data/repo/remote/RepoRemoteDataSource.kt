package br.com.pebmed.data.repo.remote

import br.com.pebmed.data.repo.remote.model.GetReposResponse
import br.com.pebmed.data.base.BaseDataSourceImpl
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper

class RepoRemoteDataSource(
    private val repoApi: RepoApi
) : BaseDataSourceImpl() {
    suspend fun getRepos(
        page: Int,
        language: String
    ): CompleteResultWrapper<GetReposResponse, BaseErrorData<Unit>> {
        return safeApiCall { repoApi.getRepos(page, language) }
    }
}