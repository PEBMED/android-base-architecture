package br.com.pebmed.data.repository

import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.data.remote.model.response.UserResponse
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.entities.User
import java.util.*

object DataUsefulObjects {
    fun loadUserResponse() = UserResponse(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequestResponse(userResponse: UserResponse) = PullRequestResponse(
        htmlUrl = "http://the.url",
        createdAt = Date(),
        body = "Body",
        title = "Title",
        user = userResponse
    )
}
