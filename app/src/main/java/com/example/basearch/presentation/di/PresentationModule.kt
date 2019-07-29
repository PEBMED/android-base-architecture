package com.example.basearch.presentation.di

import com.example.basearch.presentation.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(
        getReposUseCase = get()
    ) }
}