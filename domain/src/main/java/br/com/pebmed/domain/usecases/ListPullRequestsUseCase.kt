package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class ListPullRequestsUseCase (
    private val pullRequestRepository: PullRequestRepository
) : BaseUseCase<ResultWrapper<List<PullRequest>?, String>, ListPullRequestsUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<List<PullRequest>?, String> {
        val resultWrapper = pullRequestRepository.listPullRequests(
            owner = params.owner,
            repoName = params.repoName
        )

        return when (resultWrapper) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(resultWrapper.data)
            }

            is ResultWrapper.Error -> {
                ResultWrapper.Error(resultWrapper.data?.errorMessage)
            }
        }
    }

    data class Params(
        val owner: String,
        val repoName: String
    )
}