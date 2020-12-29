package br.com.pebmed.data.pullrequest

import br.com.pebmed.data.pullRequest.PullRequestRemoteDataSource
import br.com.pebmed.data.pullrequest.model.MockPullRequestResponseModel
import br.com.pebmed.domain.base.CompleteResultWrapper
import io.mockk.coEvery

class MockPullRequestRemoteDataSource(val mock: PullRequestRemoteDataSource) {

    fun mockGetPullRequestListWithOneItem() {
        coEvery {
            mock.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
                success = listOf(MockPullRequestResponseModel.generic())
        )
    }
}