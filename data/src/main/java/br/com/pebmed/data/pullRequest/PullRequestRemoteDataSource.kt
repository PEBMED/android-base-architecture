package br.com.pebmed.data.pullRequest

import br.com.pebmed.data.base.BaseDataSourceImpl
import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper

class PullRequestRemoteDataSource(
    private val pullRequestApi: PullRequestApi
) : BaseDataSourceImpl() {
    suspend fun getPullRequests(
        owner: String,
        repoName: String
    ): CompleteResultWrapper<List<PullRequestResponseModel>, BaseErrorData<Unit>> {
        return safeApiCall { pullRequestApi.getPullRequests(owner, repoName) }
    }

    suspend fun getPullRequest(
        owner: String,
        repoName: String,
        pullRequestNumber: Long
    ): CompleteResultWrapper<PullRequestResponseModel, BaseErrorData<Unit>> {
        return safeApiCall {
            pullRequestApi.getPullRequest(
                owner,
                repoName,
                pullRequestNumber
            )
        }
    }
}