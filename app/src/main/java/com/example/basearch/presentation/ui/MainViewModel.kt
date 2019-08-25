package com.example.basearch.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basearch.domain.entities.Repo
import com.example.basearch.domain.usecases.GetReposUseCase

class MainViewModel(
    private val getReposUseCase: GetReposUseCase
) : ViewModel() {

    private val _reposState = MutableLiveData<ViewStateResource<List<Repo>>>()
    val reposState: LiveData<ViewStateResource<List<Repo>>>
        get() = _reposState

    init {
        loadRepos()
    }

    fun loadRepos() {
        _reposState.postValue(ViewStateResource.Loading())

        val params = GetReposUseCase.Params(true)
        getReposUseCase.invoke(viewModelScope, params) {
            run {
                _reposState.postValue(it)
            }
        }
    }
}