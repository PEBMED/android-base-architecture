package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class ListPullRequestsUseCase (
    private val pullRequestRepository: PullRequestRepository
) {

    suspend fun run(params: Params): ResultWrapper<List<PullRequest>?, BaseErrorData<String>?> {
        return pullRequestRepository.listPullRequests(
            owner = params.owner,
            repoName = params.repoName
        )
    }

    data class Params(
        val owner: String,
        val repoName: String
    )
}