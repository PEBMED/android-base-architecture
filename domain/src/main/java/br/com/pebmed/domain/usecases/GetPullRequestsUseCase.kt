package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.BaseAsyncUseCase
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.repository.PullRequestRepository
import br.com.pebmed.domain.usecases.GetPullRequestsUseCase.Params

class GetPullRequestsUseCase(
    private val pullRequestRepository: PullRequestRepository
) : BaseAsyncUseCase<ResultWrapper<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>, Params>() {

    override suspend fun runAsync(params: Params): ResultWrapper<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>> {
        return pullRequestRepository.getPullRequests(
            owner = params.owner,
            repoName = params.repoName
        ).transformError { BaseErrorData(errorBody = BaseErrorStatus.DEFAULT_ERROR) }
    }

    data class Params(
        val owner: String,
        val repoName: String
    )
}