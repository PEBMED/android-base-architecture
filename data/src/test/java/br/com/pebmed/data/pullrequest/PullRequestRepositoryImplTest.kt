package br.com.pebmed.data.pullrequest

import br.com.pebmed.data.pullRequest.PullRequestRepositoryImpl
import io.mockk.*
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PullRequestRepositoryImplTest {

    private lateinit var mockPullRequestRemoteDataSource: MockPullRequestRemoteDataSource

    private lateinit var pullRequestRepositoryImpl: PullRequestRepositoryImpl

    @Before
    fun setUp() {
        mockPullRequestRemoteDataSource = MockPullRequestRemoteDataSource(mockk())

        pullRequestRepositoryImpl = PullRequestRepositoryImpl(mockPullRequestRemoteDataSource.mock)
    }

    @Test
    fun `SHOULD list pull requests WHEN success fetched`() = runBlocking {
        //ARRANGE
        mockPullRequestRemoteDataSource.mockGetPullRequestListWithOneItem()

        //ACT
        val result = pullRequestRepositoryImpl.getPullRequests(owner = "", repoName = "")

        //ASSERT
        coVerify {
            mockPullRequestRemoteDataSource.mock.getPullRequests(any(), any())
        }
        confirmVerified(mockPullRequestRemoteDataSource)

        assertNotNull(result.success)
    }
}