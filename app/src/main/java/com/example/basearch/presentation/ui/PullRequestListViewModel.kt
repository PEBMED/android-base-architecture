package com.example.basearch.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase

class PullRequestListViewModel(
    private val listPullRequestsUseCase: ListPullRequestsUseCase
) : ViewModel() {

    private val _pullRequestListState = MutableLiveData<ViewStateResource<List<PullRequest>>>()

    val pullRequestlistState: LiveData<ViewStateResource<List<PullRequest>>>
        get() = _pullRequestListState

    fun loadPullRequestList(
        owner: String,
        repoName: String
    ) {
        _pullRequestListState.postValue(ViewStateResource.Loading())

        val params = this.loadParams(owner, repoName)

        listPullRequestsUseCase.invoke(viewModelScope, params) {
            run {
                val viewStateResource = when (it) {
                    is ResultWrapper.Success -> {
                        if (it.data != null && it.data!!.isNotEmpty())
                            ViewStateResource.Success(it.data!!)
                        else
                            ViewStateResource.Empty<List<PullRequest>>()
                    }

                    is ResultWrapper.Error -> {
                        ViewStateResource.Error(it.data)
                    }
                }

                _pullRequestListState.postValue(viewStateResource)
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