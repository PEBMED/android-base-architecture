package com.example.basearch.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import com.example.basearch.presentation.extensions.loadViewStateResourceList
import com.example.basearch.presentation.ui.ViewStateResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        viewModelScope.launch(Dispatchers.IO) {
            val resultWrapper = listPullRequestsUseCase.run(params)
            val loadViewStateResourceList = loadViewStateResourceList(resultWrapper)
            _pullRequestListState.postValue(loadViewStateResourceList)
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