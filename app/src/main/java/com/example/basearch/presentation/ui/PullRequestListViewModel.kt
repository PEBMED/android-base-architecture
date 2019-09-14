package com.example.basearch.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import com.example.basearch.presentation.extensions.loadViewStateResourceList

class PullRequestListViewModel(
    private val listPullRequestsUseCase: ListPullRequestsUseCase
) : ViewModel() {

    private val _pullRequestListState = MutableLiveData<ViewStateResource<List<PullRequest>?, BaseErrorData<String>?>>()

    val pullRequestListState: LiveData<ViewStateResource<List<PullRequest>?, BaseErrorData<String>?>>
        get() = _pullRequestListState

    fun loadPullRequestList(
        owner: String,
        repoName: String
    ) {
        _pullRequestListState.postValue(ViewStateResource.Loading())

        val params = this.loadParams(owner, repoName)
        listPullRequestsUseCase.invoke(viewModelScope, params) { resultWrapper ->
            run {
                _pullRequestListState.postValue(this.loadViewStateResourceList(resultWrapper))
            }
        }
    }

    private fun loadParams(
        owner: String,
        repoName: String
    ) = ListPullRequestsUseCase.Params(
        owner = owner,
        repoName = repoName
    )
}