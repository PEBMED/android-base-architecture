package br.com.pebmed.data.repository

import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.data.remote.source.PullRequestRemoteDataSource
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GetReposErrorData
import br.com.pebmed.domain.entities.GitHubErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class PullRequestRepositoryImpl(
    private val pullRequestRemoteDataSource: PullRequestRemoteDataSource
) : PullRequestRepository {
    override suspend fun listPullRequests(
        owner: String,
        repoName: String
    ): ResultWrapper<List<PullRequest>?, BaseErrorData<String>?> {
        val listPullRequests = pullRequestRemoteDataSource.listPullRequests(owner, repoName)

        return listPullRequests.transform(
            functionSuccess(),
            function()
        )
    }

    private fun functionSuccess(): (List<PullRequestResponse>) -> List<PullRequest> {
        return {
            it.map { pullRequestResponse ->
                pullRequestResponse.mapTo()
            }
        }
    }

    private fun function(): (BaseErrorData<GitHubErrorData>?) -> BaseErrorData<String>? {
        return { baseErrorData ->
            BaseErrorData(
                errorBody = baseErrorData?.errorBody?.message,
                errorMessage = baseErrorData?.errorMessage
            )
        }
    }
}