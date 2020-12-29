package com.pebmed.basearch.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.pebmed.domain.*
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.GetPullRequestListUseCase
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.basearch.presentation.ui.pullRequest.list.PullRequestListViewModel
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * What to test?
 * 1 - If all functions are called in correct order
 * 2 - If pass the correct params to the respective UseCase
 * 3 - If the flow follow the correct way
 */
class PullRequestListViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @MockK(relaxUnitFun = true)
    private lateinit var mockGetPullRequestsUseCase: MockGetPullRequestListUseCase

    private lateinit var pullRequest: PullRequestModel

    private lateinit var params: GetPullRequestListUseCase.Params

    private lateinit var viewModel : PullRequestListViewModel

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        MockKAnnotations.init(this)

        mockGetPullRequestsUseCase = MockGetPullRequestListUseCase(mockk())
        this.pullRequest = MockPullRequestModel.generic()
        this.params = MockGetPullRequestListUseCase.mockGenericParams()

        Dispatchers.setMain(testDispatcher)

        viewModel = PullRequestListViewModel(
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
    fun `SHOULD return success state WHEN fetch pull request success`() = testDispatcher.runBlockingTest {

        val resultList = MockPullRequestModel.mockListWithOneGenericItem()

        mockGetPullRequestsUseCase.mockSuccess()

        val testObserver = viewModel.pullRequestListState.test()

        viewModel.loadPullRequestList(params.owner, params.repoName)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val successValue = valueHistory[1]
        assertTrue(successValue is ViewState.Success)
        assertEquals(resultList[0].number, (successValue as ViewState.Success).data[0].number)
        assertEquals(resultList[0].body, (successValue as ViewState.Success).data[0].body)
        assertEquals(resultList[0].additions, (successValue as ViewState.Success).data[0].additions)
        assertEquals(resultList[0].comments, (successValue as ViewState.Success).data[0].comments)
        assertEquals(resultList[0].commits, (successValue as ViewState.Success).data[0].commits)
        assertEquals(resultList[0].htmlUrl, (successValue as ViewState.Success).data[0].htmlUrl)
        assertEquals(resultList[0].deletions, (successValue as ViewState.Success).data[0].deletions)
        assertEquals(resultList[0].changedFiles, (successValue as ViewState.Success).data[0].changedFiles)
        assertEquals(resultList[0].createdAt, (successValue as ViewState.Success).data[0].createdAt)
        assertEquals(resultList[0].title, (successValue as ViewState.Success).data[0].title)
        assertEquals(resultList[0].user.login, (successValue as ViewState.Success).data[0].user.login)
        assertEquals(resultList[0].user.avatarUrl, (successValue as ViewState.Success).data[0].user.avatarUrl)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD return empty state WHEN pull request list is empty`() = testDispatcher.runBlockingTest {

        mockGetPullRequestsUseCase.mockSuccessWithEmptyList()

        val testObserver = viewModel.pullRequestListState.test()

        viewModel.loadPullRequestList(params.owner, params.repoName)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val emptyValue = valueHistory[1]
        assertTrue(emptyValue is ViewState.Empty)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD return DEFAULT error state WHEN fetch pull requests error`() = testDispatcher.runBlockingTest {

        val expectedError = BaseErrorData(BaseErrorStatus.DEFAULT_ERROR)

        mockGetPullRequestsUseCase.mockError()

        val testObserver = viewModel.pullRequestListState.test()

        viewModel.loadPullRequestList(params.owner, params.repoName)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val errorValue = valueHistory[1]
        assertTrue(errorValue is ViewState.Error)
        assertEquals(expectedError, (errorValue as ViewState.Error).error)
    }
}