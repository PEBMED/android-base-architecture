package com.example.basearch.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import com.example.basearch.presentation.extensions.loadViewState
import com.example.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PullRequestViewModel(
    private val getPullRequestUseCase: GetPullRequestUseCase
) : ViewModel() {

    private val _pullRequestState = MutableLiveData<ViewState<PullRequest, BaseErrorData<String>?>>()

    val pullRequestState: LiveData<ViewState<PullRequest, BaseErrorData<String>?>>
        get() = _pullRequestState

    fun getPullRequest(
        owner: String,
        repoName: String,
        pullRequestNumber: Long
    ) {
        _pullRequestState.postValue(ViewState.Loading())

        val params = this.loadParams(owner, repoName, pullRequestNumber)

        viewModelScope.launch(Dispatchers.IO) {
            val resultWrapper = getPullRequestUseCase.run(params)

            val viewState = loadViewState(resultWrapper)
            _pullRequestState.postValue(viewState)
        }
    }

    internal fun loadParams(
        owner: String,
        repoName: String,
        pullRequestNumber: Long
    ) = GetPullRequestUseCase.Params(
        owner = owner,
        repoName = repoName,
        pullRequestNumber = pullRequestNumber
    )
}