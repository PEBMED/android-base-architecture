package com.example.basearch.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.usecases.GetReposUseCase
import com.example.basearch.presentation.extensions.loadViewState
import com.example.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val getReposUseCase: GetReposUseCase) : ViewModel() {
    private val _reposState =
        MutableLiveData<ViewState<List<RepoModel>, BaseErrorData<BaseErrorStatus>>>()
    val reposState: LiveData<ViewState<List<RepoModel>, BaseErrorData<BaseErrorStatus>>>
        get() = _reposState

    init {
        loadRepos()
    }

    fun loadRepos() {
        _reposState.postValue(ViewState.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val params = GetReposUseCase.Params(true)
            val resultWrapper = getReposUseCase.runAsync(params)

            val viewState = loadViewState(resultWrapper)
            _reposState.postValue(viewState)
        }
    }
}