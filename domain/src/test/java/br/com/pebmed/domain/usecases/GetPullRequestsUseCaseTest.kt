package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.MockGetPullRequestsUseCase
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.repository.MockPullRequestRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPullRequestsUseCaseTest {

    private lateinit var mockPullRequestRepository: MockPullRequestRepository

    private lateinit var getPullRequestsUseCase: GetPullRequestsUseCase

    @Before
    fun setUp() {
        mockPullRequestRepository = MockPullRequestRepository(mockk())

        getPullRequestsUseCase = GetPullRequestsUseCase(mockPullRequestRepository.mock)
    }

    /**
     * This test was added just for reference purposes
     */
    @Test
    fun `SHOULD return pull requests list WHEN success fetched`() = runBlocking {
        mockPullRequestRepository.mockGetPullRequestsListWithOneItem()

        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockGenericParams())

        assertEquals("luis.fernandez", resultWrapper.success?.get(0)?.user?.login)
    }

    @Test
    fun `SHOULD return default error WHEN error fetched`() = runBlocking {
        mockPullRequestRepository.mockGetPullRequestWithBaseErrorDataUnit()

        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockGenericParams())

        assertEquals(BaseErrorStatus.DEFAULT_ERROR, resultWrapper.error?.errorBody)
    }

    @Test
    fun `SHOULD call correct dependency function WHEN run`() = runBlocking {
        mockPullRequestRepository.mockGetPullRequestsListWithOneItem()


        getPullRequestsUseCase.runAsync(MockGetPullRequestsUseCase.mockGenericParams())

        coVerify {
            mockPullRequestRepository.mock.getPullRequests(any(), any())
        }

        confirmVerified(mockPullRequestRepository.mock)
    }
}
