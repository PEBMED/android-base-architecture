package com.example.basearch.presentation.di

import com.example.basearch.presentation.ui.viewmodel.MainViewModel
import com.example.basearch.presentation.ui.viewmodel.PullRequestListViewModel
import com.example.basearch.presentation.ui.viewmodel.PullRequestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(
            getReposUseCase = get()
        )
    }

    viewModel {
        PullRequestListViewModel(
            listPullRequestsUseCase = get()
        )
    }

    viewModel {
        PullRequestViewModel(
            getPullRequestUseCase = get()
        )
    }
}