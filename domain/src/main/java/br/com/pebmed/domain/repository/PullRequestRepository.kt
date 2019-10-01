package br.com.pebmed.domain.repository

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest

interface PullRequestRepository {
    suspend fun listPullRequests(
        owner: String,
        repoName: String
    ) : CompleteResultWrapper<List<PullRequest>, BaseErrorData<String>?>
}