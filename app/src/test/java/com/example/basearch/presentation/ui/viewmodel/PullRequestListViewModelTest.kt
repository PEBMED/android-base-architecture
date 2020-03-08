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
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
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

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxUnitFun = true)
    private lateinit var getPullRequestsUseCase: GetPullRequestsUseCase

    private lateinit var pullRequest: PullRequestModel

    private lateinit var params: GetPullRequestsUseCase.Params

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        this.pullRequest = FakePullRequestModel.mock(FakeUserModel.mock())
        this.params = FakeGetPullRequestsUseCase.Params.mock()
    }

    @Test
    fun `SHOULD handle success state`() {
        val viewModel =
            PullRequestListViewModel(
                getPullRequestsUseCase
            )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()

        val resultWrapper = FakeResultWrapper.mockSuccess<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
            FakePullRequestModel.mockList(1)
        )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns resultWrapper

        viewModel.loadPullRequestList("OwnerModel", "RepoName")

        testObserver
            .assertValue {
                it is ViewState.Loading
            }
            .assertHistorySize(1)
            .awaitNextValue()
            .assertHistorySize(2)
            .assertValue {
                it is ViewState.Success
            }
            .assertValue {
                if (it is ViewState.Success) {
                    it.data[0].htmlUrl == resultWrapper.success?.get(0)?.htmlUrl
                } else {
                    false
                }
            }
    }

    @Test
    fun `SHOULD handle empty state`() {
        val viewModel = PullRequestListViewModel(
            getPullRequestsUseCase
        )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()

        val emptyResultWrapper = FakeResultWrapper.mockSuccess<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
            success = listOf()
        )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns emptyResultWrapper

        viewModel.loadPullRequestList("OwnerModel", "RepoName")

        testObserver
            .assertValue {
                it is ViewState.Loading
            }
            .assertHistorySize(1)
            .awaitNextValue()
            .assertHistorySize(2)
            .assertValue {
                it is ViewState.Empty
            }
    }

    @Test
    fun `SHOULD handle error state`() {
        val viewModel =
            PullRequestListViewModel(
                getPullRequestsUseCase
            )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()


        val errorResultWrapper = FakeResultWrapper.mockError<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>(
            FakeBaseErrorData.mockStatusError()
        )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns errorResultWrapper

        viewModel.loadPullRequestList("OwnerModel", "RepoName")

        testObserver
            .assertValue {
                it is ViewState.Loading
            }
            .assertHistorySize(1)
            .awaitNextValue()
            .assertHistorySize(2)
            .assertValue {
                it is ViewState.Error
            }
            .assertValue {
                if (it is ViewState.Error) {
                    it.error?.errorMessage == errorResultWrapper.error?.errorMessage
                } else {
                    false
                }
            }
    }

    @Test
    fun `SHOULD call functions in correct order`() {
        val viewModel = spyk(
            PullRequestListViewModel(
                getPullRequestsUseCase
            )
        )

        coEvery {
            getPullRequestsUseCase.runAsync(params)
        } returns FakeResultWrapper.mockSuccess(
            FakePullRequestModel.mockList(1)
        )

        viewModel.loadPullRequestList("OwnerModel", "RepoName")

        coVerifyOrder {
            viewModel.loadPullRequestList("OwnerModel", "RepoName")
            getPullRequestsUseCase.runAsync(FakeGetPullRequestsUseCase.Params.mock())
        }
    }
}