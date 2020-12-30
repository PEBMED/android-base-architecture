package com.pebmed.basearch.presentation.ui.pullRequest.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.usecases.GetPullRequestListUseCase
import com.pebmed.basearch.presentation.extensions.loadViewState
import com.pebmed.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PullRequestListViewModel(
    private val dispatcherIO: CoroutineDispatcher,
    private val getPullRequestsUseCase: GetPullRequestListUseCase
) : ViewModel() {
    private val _pullRequestListState =
        MutableLiveData<ViewState<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>>()
    val pullRequestListState: LiveData<ViewState<List<PullRequestModel>, BaseErrorData<BaseErrorStatus>>>
        get() = _pullRequestListState

    fun loadPullRequestList(owner: String, repoName: String) {
        viewModelScope.launch {
            _pullRequestListState.postValue(ViewState.Loading())

            val resultWrapper = withContext(dispatcherIO) {
                val params = GetPullRequestListUseCase.Params(owner, repoName)
                getPullRequestsUseCase.runAsync(params)
            }

            val viewState = loadViewState(resultWrapper)
            _pullRequestListState.postValue(viewState)
        }
    }
}