package com.example.basearch.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import com.example.basearch.presentation.extensions.loadViewState
import com.example.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PullRequestListViewModel(
    private val listPullRequestsUseCase: ListPullRequestsUseCase
) : ViewModel() {

    private val _pullRequestListState = MutableLiveData<ViewState<List<PullRequest>, BaseErrorData<String>?>>()

    val pullRequestListState: LiveData<ViewState<List<PullRequest>, BaseErrorData<String>?>>
        get() = _pullRequestListState

    fun loadPullRequestList(
        owner: String,
        repoName: String
    ) {
        _pullRequestListState.postValue(ViewState.Loading())

        val params = this.loadParams(owner, repoName)

        viewModelScope.launch(Dispatchers.IO) {
            val resultWrapper = listPullRequestsUseCase.run(params)

            val viewState = loadViewState(resultWrapper)
            _pullRequestListState.postValue(viewState)
        }
    }

    internal fun loadParams(
        owner: String,
        repoName: String
    ) = ListPullRequestsUseCase.Params(
        owner = owner,
        repoName = repoName
    )
}