package com.example.basearch.presentation.ui.viewmodel

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.entities.User
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import java.util.*

object UsefulObjects {
    fun loadUser() = User(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequest() = PullRequest(
        number = 1,
        htmlUrl = "http://the.url",
        title = "Title",
        user = this.loadUser(),
        body = "Body",
        createdAt = Date(),
        comments = 1,
        commits = 1,
        additions = 1,
        deletions = 1,
        changedFiles = 1
    )

    fun loadListPullRequestsUseCaseParams() = ListPullRequestsUseCase.Params(
        owner = "Owner",
        repoName = "RepoName"
    )

    fun loadSuccessResultWrapper() =  CompleteResultWrapper<List<PullRequest>, BaseErrorData<String>?>(
        success = listOf(
            this.loadPullRequest()
        )
    )

    fun loadEmptyResultWrapper() =  CompleteResultWrapper<List<PullRequest>, BaseErrorData<String>?>(
        success = listOf()
    )

    fun loadErrorResultWrapper() =  CompleteResultWrapper<List<PullRequest>, BaseErrorData<String>?>(
        error = BaseErrorData(
            errorMessage = "Test Error"
        )
    )
}
