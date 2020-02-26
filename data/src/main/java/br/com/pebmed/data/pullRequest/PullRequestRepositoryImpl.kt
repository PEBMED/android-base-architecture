package br.com.pebmed.data.pullRequest

import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.repository.PullRequestRepository

class PullRequestRepositoryImpl(
    private val pullRequestRemoteDataSource: PullRequestRemoteDataSource
) : PullRequestRepository {
    override suspend fun getPullRequests(
        owner: String,
        repoName: String
    ): CompleteResultWrapper<List<PullRequestModel>, BaseErrorData<Unit>> {
        val listPullRequests = pullRequestRemoteDataSource.getPullRequests(owner, repoName)

        return listPullRequests.transformSuccess(handleGetPullRequestsSuccess())
    }

    override suspend fun getPullRequest(
        owner: String,
        repoName: String,
        pullRequestNumber: Long
    ): CompleteResultWrapper<PullRequestModel, BaseErrorData<Unit>> {
        val pullRequest =
            pullRequestRemoteDataSource.getPullRequest(owner, repoName, pullRequestNumber)

        return pullRequest.transformSuccess { it.mapTo() }
    }

    private fun handleGetPullRequestsSuccess(): (List<PullRequestResponseModel>) -> List<PullRequestModel> {
        return {
            it.map { pullRequestResponse ->
                pullRequestResponse.mapTo()
            }
        }
    }
}