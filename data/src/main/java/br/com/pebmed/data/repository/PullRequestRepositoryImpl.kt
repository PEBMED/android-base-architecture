package br.com.pebmed.data.repository

import br.com.pebmed.data.remote.mapper.PullRequestRemoteMapper
import br.com.pebmed.data.remote.source.PullRequestRemoteDataSource
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.repository.PullRequestRepository

class PullRequestRepositoryImpl(
    private val pullRequestRemoteDataSource: PullRequestRemoteDataSource
) : PullRequestRepository {
    override suspend fun listPullRequests(
        owner: String,
        repoName: String
    ): ResultWrapper<List<PullRequest>, BaseErrorData<Void>> {
        return when (val remoteResult = pullRequestRemoteDataSource.listPullRequests(owner, repoName)) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(remoteResult.data.map { PullRequestRemoteMapper.map(it) })
            }

            is ResultWrapper.Error -> {
                remoteResult.transformError()
            }
        }
    }
}