package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.FakeGetPullRequestsUseCase
import br.com.pebmed.domain.FakePullRequestModel
import br.com.pebmed.domain.FakeUserModel
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.UserModel
import br.com.pebmed.domain.repository.PullRequestRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ListPullRequestsUseCaseTest {

    @MockK
    private lateinit var pullRequestRepository: PullRequestRepository

    private lateinit var user: UserModel
    private lateinit var pullRequest: PullRequestModel
    private lateinit var params: GetPullRequestsUseCase.Params

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        user = FakeUserModel.mock()
        pullRequest = FakePullRequestModel.mock(user)
        params = FakeGetPullRequestsUseCase.Params.mock()
    }

    /**
     * This test was added just for reference purposes
     */
    @Test
    fun `SHOULD return the correct success object`() = runBlocking {
        coEvery {
            pullRequestRepository.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        val resultWrapper =
            GetPullRequestsUseCase(pullRequestRepository).runAsync(params)

        assertEquals("luis.fernandez", resultWrapper.success?.get(0)?.user?.login)
    }

    @Test
    fun `SHOULD call correct dependency function WHEN run`() = runBlocking {
        coEvery {
            pullRequestRepository.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        GetPullRequestsUseCase(pullRequestRepository).runAsync(params)

        coVerify {
            pullRequestRepository.getPullRequests(any(), any())
        }

        confirmVerified(pullRequestRepository)
    }

    @Test
    fun testTheTest() {
        val two = mockk<Two>()

        every {
            two.needsToExecute()
        } returns 2

        val one = One(two)
        one.test()

        verify(exactly = 1) {
            two.needsToExecute()
        }

        confirmVerified(two)
    }

    @After
    fun tearDown() {
    }

    class One(
        private val two: Two
    ) {
        fun test() {
            two.needsToExecute()
        }
    }

    class Two {
        fun needsToExecute() = 1092
    }
}
