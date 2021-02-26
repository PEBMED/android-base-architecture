package com.pebmed.basearch.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.PaginationData
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.usecases.GetReposUseCase
import com.pebmed.basearch.presentation.extensions.loadViewState
import com.pebmed.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val getReposUseCase: GetReposUseCase) : ViewModel() {
    private val _reposState =
        MutableLiveData<ViewState<Pair<List<RepoModel>, PaginationData?>, BaseErrorData<BaseErrorStatus>>>()
    val reposState: LiveData<ViewState<Pair<List<RepoModel>, PaginationData?>, BaseErrorData<BaseErrorStatus>>>
        get() = _reposState

    init {
        loadRepos()
    }

    fun loadRepos(page: Int = 1) {
        _reposState.postValue(ViewState.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val params = GetReposUseCase.Params(true, page)
            val resultWrapper = getReposUseCase.runAsync(params)

            val viewState = loadViewState(resultWrapper)
            _reposState.postValue(viewState)
        }
    }
}