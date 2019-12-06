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
}
