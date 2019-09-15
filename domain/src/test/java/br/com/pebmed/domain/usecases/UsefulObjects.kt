package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.entities.User
import java.util.*

object UsefulObjects {
    fun loadUser() = User(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequest(user: User) = PullRequest(
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
}
