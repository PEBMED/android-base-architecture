package com.example.basearch.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.entities.GetReposErrorData
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.usecases.GetReposUseCase
import com.example.basearch.presentation.extensions.loadViewStateResourceList

class MainViewModel(
    private val getReposUseCase: GetReposUseCase
) :ViewModel() {

    private val _reposState = MutableLiveData<ViewStateResource<List<Repo>?, BaseErrorData<GetReposErrorData>?>>()
    val reposState: LiveData<ViewStateResource<List<Repo>?, BaseErrorData<GetReposErrorData>?>>
        get() = _reposState

    init {
        loadRepos()
    }

    fun loadRepos() {
        _reposState.postValue(ViewStateResource.Loading())

        val params = GetReposUseCase.Params(true)
        getReposUseCase.invoke(viewModelScope, params) { resultWrapper ->
            run {
                _reposState.postValue(this.loadViewStateResourceList(resultWrapper))
            }
        }
    }
}