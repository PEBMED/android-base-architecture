package br.com.pebmed.data.pullrequest

import br.com.pebmed.data.pullRequest.PullRequestRemoteDataSource
import br.com.pebmed.data.pullRequest.PullRequestRepositoryImpl
import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import br.com.pebmed.data.pullrequest.model.mock.MockPullRequestResponseModel
import br.com.pebmed.domain.base.CompleteResultWrapper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
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