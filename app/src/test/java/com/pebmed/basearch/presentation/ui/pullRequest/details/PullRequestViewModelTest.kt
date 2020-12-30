package com.pebmed.basearch.presentation.ui.pullRequest.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.pebmed.domain.MockGetPullRequestUseCase
import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.MockUserModel
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import com.jraska.livedata.test
import com.pebmed.basearch.presentation.ui.base.ViewState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

class PullRequestViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK(relaxUnitFun = true)
    private lateinit var mockGetPullRequestsUseCase: MockGetPullRequestUseCase

    private lateinit var pullRequest: PullRequestModel

    private lateinit var params: GetPullRequestUseCase.Params

    private lateinit var viewModel : PullRequestViewModel

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        MockKAnnotations.init(this)

        mockGetPullRequestsUseCase = MockGetPullRequestUseCase(mockk())
        this.pullRequest = MockPullRequestModel.generic()

        Dispatchers.setMain(testDispatcher)

        params = mockGetPullRequestsUseCase.params()

        viewModel = PullRequestViewModel(
            testDispatcher,
            mockGetPullRequestsUseCase.mock
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD return single pull request WHEN success state in correct order`() = testDispatcher.runBlockingTest {

        val result = MockPullRequestModel.generic()

        mockGetPullRequestsUseCase.mockSuccess()

        val testObserver = viewModel.pullRequestState.test()

        viewModel.getPullRequest(params.owner, params.repoName, params.pullRequestNumber)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val successValue = valueHistory[1]
        assertTrue(successValue is ViewState.Success)

        val successData = (successValue as ViewState.Success).data

        assertEquals(result.number        , successData.number)
        assertEquals(result.body          , successData.body)
        assertEquals(result.additions     , successData.additions)
        assertEquals(result.comments      , successData.comments)
        assertEquals(result.commits       , successData.commits)
        assertEquals(result.htmlUrl       , successData.htmlUrl)
        assertEquals(result.deletions     , successData.deletions)
        assertEquals(result.changedFiles  , successData.changedFiles)
        assertEquals(result.createdAt     , successData.createdAt)
        assertEquals(result.title         , successData.title)
        assertEquals(result.user.login    , successData.user.login)
        assertEquals(result.user.avatarUrl, successData.user.avatarUrl)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD return DEFAULT error WHEN error state`() = testDispatcher.runBlockingTest {

        mockGetPullRequestsUseCase.mockError()

        val testObserver = viewModel.pullRequestState.test()

        viewModel.getPullRequest(params.owner, params.repoName, params.pullRequestNumber)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val emptyValue = valueHistory[1]
        assertTrue(emptyValue is ViewState.Error)
    }
}