package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseUseCase
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class GetPullRequestUseCase(
    private val pullRequestRepository: PullRequestRepository
) : BaseUseCase<ResultWrapper<PullRequest, String?>, GetPullRequestUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<PullRequest, String?> {
        return pullRequestRepository.getPullRequest(
            owner = params.owner,
            repoName = params.repoName,
            pullRequestNumber = params.pullRequestNumber
        ).transformError { it?.errorMessage }
    }

    data class Params(
        val owner: String,
        val repoName: String,
        val pullRequestNumber: Long
    )
}