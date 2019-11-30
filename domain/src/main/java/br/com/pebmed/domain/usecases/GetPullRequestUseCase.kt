package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class GetPullRequestUseCase (
    private val pullRequestRepository: PullRequestRepository
) {

    suspend fun run(params: Params): CompleteResultWrapper<PullRequest, BaseErrorData<String>?> {
        return pullRequestRepository.getPullRequest(
            owner = params.owner,
            repoName = params.repoName,
            pullRequestId = params.pullRequestId
        )
    }

    data class Params(
        val owner: String,
        val repoName: String,
        val pullRequestId: Int
    )
}