package br.com.pebmed.data.remote.source

import br.com.pebmed.data.remote.api.PullRequestApi
import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.data.repository.BaseDataSourceImpl
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GitHubErrorData

class PullRequestRemoteDataSourceImpl(
    private val pullRequestApi: PullRequestApi
) : PullRequestRemoteDataSource, BaseDataSourceImpl() {

    override suspend fun listPullRequests(
        owner: String,
        repoName: String
    ): CompleteResultWrapper<List<PullRequestResponse>, BaseErrorData<GitHubErrorData>> {
        return safeApiCall { pullRequestApi.listPullRequestsAsync(owner, repoName) }

    }
}