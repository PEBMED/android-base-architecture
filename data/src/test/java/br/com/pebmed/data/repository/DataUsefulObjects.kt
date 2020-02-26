package br.com.pebmed.data.repository

import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.data.pullRequest.model.UserResponseModel
import java.util.*

object DataUsefulObjects {
    fun loadUserResponse() = UserResponseModel(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequestResponse(userResponseModel: UserResponseModel) =
        PullRequestResponseModel(
            number = 1,
            htmlUrl = "http://the.url",
            title = "Title",
            userModel = userResponseModel,
            body = "Body",
            createdAt = Date(),
            comments = 1,
            commits = 1,
            additions = 1,
            deletions = 1,
            changedFiles = 1
        )
}
