package com.example.basearch.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.usecases.GetReposUseCase

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
                val viewStateResource = when(it) {
                    is ResultWrapper.Success -> {
                        if (it.data != null && it.data!!.isNotEmpty())
                            ViewStateResource.Success(it.data!!)
                        else
                            ViewStateResource.Empty<List<Repo>>()
                    }

                    is ResultWrapper.Error -> {
                        ViewStateResource.Error(it.data)
                    }
                }

                _reposState.postValue(viewStateResource)
            }
        }
    }
}