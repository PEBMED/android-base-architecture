package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.MockGetPullRequestListUseCase
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.repository.MockPullRequestRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPullRequestsUseCaseTest {

    private lateinit var mockPullRequestRepository: MockPullRequestRepository

    private lateinit var getPullRequestsUseCase: GetPullRequestListUseCase

    @Before
    fun setUp() {
        mockPullRequestRepository = MockPullRequestRepository(mockk())

        getPullRequestsUseCase = GetPullRequestListUseCase(mockPullRequestRepository.mock)
    }

    /**
     * This test was added just for reference purposes
     */
    @Test
    fun `SHOULD return pull requests list WHEN success fetched`() = runBlocking {
        //ARRANGE
        mockPullRequestRepository.mockGetPullRequestsListWithOneItem()

        //ACT
        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestListUseCase.mockGenericParams())

        //ASSERT
        assertEquals("luis.fernandez", resultWrapper.success?.get(0)?.user?.login)
    }

    @Test
    fun `SHOULD return default error WHEN error fetched`() = runBlocking {
        //ARRANGE
        mockPullRequestRepository.mockGetPullRequestWithBaseErrorDataUnit()

        //ACT
        val resultWrapper = getPullRequestsUseCase.runAsync(MockGetPullRequestListUseCase.mockGenericParams())

        //ASSERT
        assertEquals(BaseErrorStatus.DEFAULT_ERROR, resultWrapper.error?.errorBody)
    }

    @Test
    fun `SHOULD call correct dependency function WHEN run`() = runBlocking {
        //ARRANGE
        mockPullRequestRepository.mockGetPullRequestsListWithOneItem()

        //ACT
        getPullRequestsUseCase.runAsync(MockGetPullRequestListUseCase.mockGenericParams())

        //ASSERT
        coVerify {
            mockPullRequestRepository.mock.getPullRequests(any(), any())
        }

        confirmVerified(mockPullRequestRepository.mock)
    }
}
