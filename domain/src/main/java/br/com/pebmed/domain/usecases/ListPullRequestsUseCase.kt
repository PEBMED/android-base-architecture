package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseUseCase
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase.Params

class ListPullRequestsUseCase(
    private val pullRequestRepository: PullRequestRepository
) : BaseUseCase<ResultWrapper<List<PullRequest>, String?>, Params>() {

    override suspend fun run(params: Params): ResultWrapper<List<PullRequest>, String?> {
        return pullRequestRepository.listPullRequests(
            owner = params.owner,
            repoName = params.repoName
        ).transformError { it?.errorMessage }
    }

    data class Params(
        val owner: String,
        val repoName: String
    )
}