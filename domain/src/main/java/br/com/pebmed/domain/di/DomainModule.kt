package com.example.basearch.domain.di

import com.example.basearch.domain.usecases.GetReposUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        GetReposUseCase(
            repoRepository = get()
        )
    }
}

val domainModule = listOf(useCaseModule)