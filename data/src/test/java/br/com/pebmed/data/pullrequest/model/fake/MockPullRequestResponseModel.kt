package br.com.pebmed.data.pullrequest.model.fake

import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.data.pullRequest.model.UserResponseModel
import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.MockUserModel
import io.mockk.every
import io.mockk.mockk
import java.util.*

class MockPullRequestResponseModel {

    companion object {
        fun loadPullRequestResponse(userResponseModel: UserResponseModel) : PullRequestResponseModel {
            val mockedPullRequestResponseModel = mockk<PullRequestResponseModel>()
            every {
                mockedPullRequestResponseModel.number
            } returns 1

            every {
                mockedPullRequestResponseModel.htmlUrl
            } returns "http://the.url"

            every {
                mockedPullRequestResponseModel.title
            } returns "Title"

            every {
                mockedPullRequestResponseModel.userModel
            } returns userResponseModel

            every {
                mockedPullRequestResponseModel.body
            } returns "Body"

            every {
                mockedPullRequestResponseModel.createdAt
            } returns Date()

            every {
                mockedPullRequestResponseModel.comments
            } returns 1

            every {
                mockedPullRequestResponseModel.commits
            } returns 1

            every {
                mockedPullRequestResponseModel.additions
            } returns 1

            every {
                mockedPullRequestResponseModel.deletions
            } returns 1

            every {
                mockedPullRequestResponseModel.changedFiles
            } returns 1

            every {
                mockedPullRequestResponseModel.mapTo()
            } returns MockPullRequestModel.mock(
                MockUserModel.mock()
            )

            return mockedPullRequestResponseModel
        }
    }
}