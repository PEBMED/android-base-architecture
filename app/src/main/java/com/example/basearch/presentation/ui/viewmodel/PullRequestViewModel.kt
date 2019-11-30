package com.example.basearch.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import com.example.basearch.presentation.extensions.loadViewStateResource
import com.example.basearch.presentation.extensions.loadViewStateResourceList
import com.example.basearch.presentation.ui.ViewStateResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PullRequestViewModel(
    private val getPullRequestUseCase: GetPullRequestUseCase
) : ViewModel() {

    private val _pullRequestState = MutableLiveData<ViewStateResource<PullRequest, BaseErrorData<String>?>>()

    val pullRequestState: LiveData<ViewStateResource<PullRequest, BaseErrorData<String>?>>
        get() = _pullRequestState

    fun getPullRequest(
        owner: String,
        repoName: String,
        pullRequestId: Int
    ) {
        _pullRequestState.postValue(ViewStateResource.Loading())

        val params = this.loadParams(owner, repoName, pullRequestId)

        viewModelScope.launch(Dispatchers.IO) {
            val resultWrapper = getPullRequestUseCase.run(params)
            val loadViewStateResource = loadViewStateResource(resultWrapper)
            _pullRequestState.postValue(loadViewStateResource)
        }
    }

    internal fun loadParams(
        owner: String,
        repoName: String,
        pullRequestId: Int
    ) = GetPullRequestUseCase.Params(
        owner = owner,
        repoName = repoName,
        pullRequestId = pullRequestId
    )
}