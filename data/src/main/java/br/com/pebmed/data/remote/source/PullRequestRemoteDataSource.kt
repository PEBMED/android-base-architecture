package br.com.pebmed.data.remote.source

import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GitHubErrorData

interface PullRequestRemoteDataSource {
    suspend fun listPullRequests(
        owner: String,
        repoName: String
    ): ResultWrapper<List<PullRequestResponse>, BaseErrorData<GitHubErrorData>>
}