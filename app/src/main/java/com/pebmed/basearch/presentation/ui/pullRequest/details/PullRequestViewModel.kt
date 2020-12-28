package com.pebmed.basearch.presentation.ui.pullRequest.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import com.pebmed.basearch.presentation.extensions.loadViewState
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.basearch.presentation.utils.GlobalEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PullRequestViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getPullRequestUseCase: GetPullRequestUseCase) : ViewModel() {
    private val _pullRequestState =
        MutableLiveData<ViewState<PullRequestModel, BaseErrorData<BaseErrorStatus>>>()
    val pullRequestState: LiveData<ViewState<PullRequestModel, BaseErrorData<BaseErrorStatus>>>
        get() = _pullRequestState

    fun getPullRequest(owner: String, repoName: String, pullRequestNumber: Long) {
        GlobalEspressoIdlingResource.increment()

        _pullRequestState.postValue(ViewState.Loading())

        viewModelScope.launch(dispatcher) {
            val params = GetPullRequestUseCase.Params(
                owner = owner,
                repoName = repoName,
                pullRequestNumber = pullRequestNumber
            )
            val resultWrapper = getPullRequestUseCase.runAsync(params)

            val viewState = loadViewState(resultWrapper)
            _pullRequestState.postValue(viewState)

            GlobalEspressoIdlingResource.decrement()
        }
    }
}