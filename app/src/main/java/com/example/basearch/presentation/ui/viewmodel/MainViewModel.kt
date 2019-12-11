package com.example.basearch.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.GetReposErrorData
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.usecases.GetReposUseCase
import com.example.basearch.presentation.extensions.loadViewState
import com.example.basearch.presentation.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val getReposUseCase: GetReposUseCase
) :ViewModel() {

    private val _reposState = MutableLiveData<ViewState<List<Repo>?, BaseErrorData<GetReposErrorData>?>>()
    val reposState: LiveData<ViewState<List<Repo>?, BaseErrorData<GetReposErrorData>?>>
        get() = _reposState

    init {
        loadRepos()
    }

    fun loadRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            _reposState.postValue(ViewState.Loading())

            val params = GetReposUseCase.Params(true)
            val resultWrapper = getReposUseCase.run(params)

            val viewState = loadViewState(resultWrapper)
            _reposState.postValue(viewState)
        }
    }
}