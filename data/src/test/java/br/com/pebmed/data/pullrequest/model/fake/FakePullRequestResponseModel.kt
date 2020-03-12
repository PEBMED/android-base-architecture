package br.com.pebmed.data.pullrequest.model.fake

import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.data.pullRequest.model.UserResponseModel
import java.util.*

class FakePullRequestResponseModel {

    companion object {

        fun loadPullRequestResponse(
            userResponseModel: UserResponseModel
        ) = PullRequestResponseModel(
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
}