package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.entities.User
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

    private lateinit var user: User
    private lateinit var pullRequest: PullRequest
    private lateinit var params: ListPullRequestsUseCase.Params

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        user = UsefulObjects.loadUser()
        pullRequest = UsefulObjects.loadPullRequest(user)
        params = UsefulObjects.loadListPullRequestsUseCaseParams()
    }

    /**
     * This test was added just for reference purposes
     */
    @Test
    fun `SHOULD return the correct success object`() = runBlocking {
        coEvery {
            pullRequestRepository.listPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        val resultWrapper =
            ListPullRequestsUseCase(pullRequestRepository).run(params)

        assertEquals("luis.fernandez", resultWrapper.success?.get(0)?.user?.login)
    }

    @Test
    fun `SHOULD call correct dependency function WHEN run`() = runBlocking {
        coEvery {
            pullRequestRepository.listPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequest)
        )

        ListPullRequestsUseCase(pullRequestRepository).run(params)

        coVerify {
            pullRequestRepository.listPullRequests(any(), any())
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
