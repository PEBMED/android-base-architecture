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

    @MockK
    private lateinit var mockPullRequestRemoteDataSource: PullRequestRemoteDataSource

    private lateinit var pullRequestModel: PullRequestResponseModel

    private lateinit var pullRequestRepositoryImpl: PullRequestRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        pullRequestModel = MockPullRequestResponseModel.generic()

        pullRequestRepositoryImpl = PullRequestRepositoryImpl(mockPullRequestRemoteDataSource)
    }

    @Test
    fun `SHOULD list pull requests WHEN success fetched`() = runBlocking {

        coEvery {
            mockPullRequestRemoteDataSource.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequestModel)
        )

        val result = PullRequestRepositoryImpl(mockPullRequestRemoteDataSource).getPullRequests(
                owner = "",
                repoName = ""
            )


        coVerify {
            mockPullRequestRemoteDataSource.getPullRequests(any(), any())
        }

        confirmVerified(mockPullRequestRemoteDataSource)

        assertNotNull(result.success)
    }

    @Test
    fun `SHOULD correct order WHEN call getPullRequests repository functions`() {

        coEvery {
            mockPullRequestRemoteDataSource.getPullRequests(any(), any())
        } returns CompleteResultWrapper(
            success = listOf(pullRequestModel)
        )

        val pullRequestRepositoryImpl =
            spyk(
                PullRequestRepositoryImpl(
                    mockPullRequestRemoteDataSource
                ), recordPrivateCalls = true)
        runBlocking {
            pullRequestRepositoryImpl.getPullRequests("", "")
        }

        coVerify {
            pullRequestRepositoryImpl.getPullRequests(any(), any())
            pullRequestRepositoryImpl["handleGetPullRequestsSuccess"]()
            //pullRequestRepositoryImpl["handleListPullRequestsError"]()
        }

        coVerifySequence {
            pullRequestRepositoryImpl.getPullRequests(any(), any())
            pullRequestRepositoryImpl["handleGetPullRequestsSuccess"]()
            //pullRequestRepositoryImpl["handleListPullRequestsError"]()
        }

        confirmVerified(
            pullRequestRepositoryImpl
        )
    }

    @After
    fun tearDown() {
    }
}