package br.com.pebmed.domain.repository

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel

interface PullRequestRepository {
    suspend fun getPullRequests(
        owner: String,
        repoName: String
    ): ResultWrapper<List<PullRequestModel>, BaseErrorData<Unit>>

    suspend fun getPullRequest(
        owner: String,
        repoName: String,
        pullRequestNumber: Long
    ): ResultWrapper<PullRequestModel, BaseErrorData<Unit>>
}