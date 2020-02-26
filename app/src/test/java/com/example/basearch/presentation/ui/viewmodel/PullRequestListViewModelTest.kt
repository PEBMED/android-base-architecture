package com.example.basearch.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
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
    private lateinit var listPullRequestsUseCase: ListPullRequestsUseCase

    private lateinit var pullRequest: PullRequestModel

    private lateinit var params: ListPullRequestsUseCase.Params

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        this.pullRequest = UsefulObjects.loadPullRequest()
        this.params = UsefulObjects.loadListPullRequestsUseCaseParams()
    }

    @Test
    fun testPullRequestListSuccessState() {
        val viewModel =
            PullRequestListViewModel(
                listPullRequestsUseCase
            )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()

        val resultWrapper = UsefulObjects.loadSuccessResultWrapper()

        coEvery {
            listPullRequestsUseCase.run(params)
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
                    it.data[0].htmlUrl == pullRequest.htmlUrl
                } else {
                    false
                }
            }
    }

    @Test
    fun testPullRequestListEmptyState() {
        val viewModel =
            PullRequestListViewModel(
                listPullRequestsUseCase
            )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()

        val emptyResultWrapper = UsefulObjects.loadEmptyResultWrapper()

        coEvery {
            listPullRequestsUseCase.run(params)
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
    fun testPullRequestListErrorState() {
        val viewModel =
            PullRequestListViewModel(
                listPullRequestsUseCase
            )

        val testObserver = viewModel.pullRequestListState.test()
        testObserver.assertNoValue()

        val errorResultWrapper = UsefulObjects.loadErrorResultWrapper()

        coEvery {
            listPullRequestsUseCase.run(params)
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
    fun test() {
        val viewModel = spyk(
            PullRequestListViewModel(
                listPullRequestsUseCase
            )
        )

        val resultWrapper = UsefulObjects.loadSuccessResultWrapper()

        coEvery {
            listPullRequestsUseCase.run(params)
        } returns resultWrapper

        viewModel.loadPullRequestList("OwnerModel", "RepoName")

        coVerifyOrder {
            viewModel.loadPullRequestList("OwnerModel", "RepoName")
            viewModel.loadParams("OwnerModel", "RepoName")
            listPullRequestsUseCase.run(UsefulObjects.loadListPullRequestsUseCaseParams())
        }
    }
}