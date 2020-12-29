package br.com.pebmed.domain.repository

import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import io.mockk.coEvery

class MockPullRequestRepository(val mock: PullRequestRepository) {

    fun mockGetPullRequestsListWithOneItem() {
        coEvery {
            mock.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
                success = listOf(MockPullRequestModel.generic())
        )
    }

    fun mockGetPullRequestWithBaseErrorDataUnit() {
        coEvery {
            mock.getPullRequests(any(), any())
        } returns ResultWrapper(
                error = BaseErrorData(Unit)
        )
    }

}