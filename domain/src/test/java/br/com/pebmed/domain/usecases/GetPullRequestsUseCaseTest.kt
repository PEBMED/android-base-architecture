package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.MockGetPullRequestsUseCase
import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.MockUserModel
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.UserModel
import br.com.pebmed.domain.repository.PullRequestRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPullRequestsUseCaseTest {

    @MockK
    private lateinit var pullRequestRepository: PullRequestRepository

    private lateinit var user: UserModel
    private lateinit var pullRequest: PullRequestModel
    private lateinit var getPullRequestsUseCase: GetPullRequestsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        user = MockUserModel.mock()
        pullRequest = MockPullRequestModel.mock(user)
        getPullRequestsUseCase = GetPullRequestsUseCase(pullRequestRepository)
    }

    /**
     * This test was added just for reference purposes
     */
    @Test
    fun `SHOULD return pull requests list WHEN success fetched`() = runBlocking {
        coEvery {
            pullRequestRepository.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockParams())

        assertEquals("luis.fernandez", resultWrapper.success?.get(0)?.user?.login)
    }

    @Test
    fun `SHOULD return default error WHEN error fetched`() = runBlocking {
        coEvery {
            pullRequestRepository.getPullRequests(any(), any())
        } returns ResultWrapper(
            error = BaseErrorData(Unit)
        )

        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockParams())

        assertEquals(BaseErrorStatus.DEFAULT_ERROR, resultWrapper.error?.errorBody)
    }

    @Test
    fun `SHOULD call correct dependency function WHEN run`() = runBlocking {
        coEvery {
            pullRequestRepository.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockParams())

        coVerify {
            pullRequestRepository.getPullRequests(any(), any())
        }

        confirmVerified(pullRequestRepository)
    }
}
