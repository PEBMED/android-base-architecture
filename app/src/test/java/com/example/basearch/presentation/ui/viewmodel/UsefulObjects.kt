package com.example.basearch.presentation.ui.viewmodel

import br.com.pebmed.domain.base.BaseErrorData
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
        htmlUrl = "http://the.url",
        createdAt = Date(),
        body = "Body",
        title = "Title",
        user = this.loadUser()
    )

    fun loadListPullRequestsUseCaseParams() = ListPullRequestsUseCase.Params(
        owner = "Owner",
        repoName = "RepoName"
    )

    fun loadSuccessResultWrapper() =  ResultWrapper<List<PullRequest>?, BaseErrorData<String>?>(
        success = listOf(
            this.loadPullRequest()
        )
    )

    fun loadEmptyResultWrapper() =  ResultWrapper<List<PullRequest>?, BaseErrorData<String>?>(
        success = listOf()
    )

    fun loadErrorResultWrapper() =  ResultWrapper<List<PullRequest>?, BaseErrorData<String>?>(
        error = BaseErrorData(
            errorMessage = "Test Error"
        )
    )
}
