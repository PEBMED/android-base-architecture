package com.example.basearch.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.pebmed.domain.*
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.GetPullRequestsUseCase
import com.example.basearch.presentation.ui.base.ViewState
import com.example.basearch.presentation.ui.pullRequest.list.PullRequestListViewModel
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
    private lateinit var getPullRequestsUseCase: GetPullRequestsUseCase

    private lateinit var pullRequest: PullRequestModel

    private lateinit var params: GetPullRequestsUseCase.Params

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        MockKAnnotations.init(this)

        this.pullRequest = MockPullRequestModel.mock(FakeUserModel.mock())
        this.params = FakeGetPullRequestsUseCase.Params.mock()

        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD handle success state in correct order`() = testDispatcher.runBlockingTest {
        val viewModel = PullRequestListViewModel(
            testDispatcher,
            getPullRequestsUseCase
        )

        val resultList = MockPullRequestModel.mockList(1)
        val resultWrapper =
            FakeResultWrapper.mockSuccess<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
                resultList
            )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns resultWrapper

        val testObserver = viewModel.pullRequestListState.test()

        viewModel.loadPullRequestList(params.owner, params.repoName)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val successValue = valueHistory[1]
        assertTrue(successValue is ViewState.Success)
        assertEquals(resultList, (successValue as ViewState.Success).data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `SHOULD handle empty state`() = testDispatcher.runBlockingTest {
        val viewModel = PullRequestListViewModel(
            testDispatcher,
            getPullRequestsUseCase
        )

        val emptyResultWrapper =
            FakeResultWrapper.mockSuccess<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
                success = listOf()
            )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns emptyResultWrapper

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
    fun `SHOULD handle error state in correct order`() = testDispatcher.runBlockingTest {
        val viewModel = PullRequestListViewModel(
            testDispatcher,
            getPullRequestsUseCase
        )

        val errorData = FakeBaseErrorData.mockStatusError()
        val errorResultWrapper =
            FakeResultWrapper.mockError<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
                errorData
            )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns errorResultWrapper

        val testObserver = viewModel.pullRequestListState.test()

        viewModel.loadPullRequestList(params.owner, params.repoName)

        testObserver.assertHasValue()
        testObserver.assertHistorySize(2)

        val valueHistory = testObserver.valueHistory()
        assertTrue(valueHistory[0] is ViewState.Loading)

        val errorValue = valueHistory[1]
        assertTrue(errorValue is ViewState.Error)
        assertEquals(errorData, (errorValue as ViewState.Error).error)
    }
}