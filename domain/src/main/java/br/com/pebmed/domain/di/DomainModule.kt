package br.com.pebmed.domain.di

import br.com.pebmed.domain.usecases.GetReposUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        GetReposUseCase(
            repoRepository = get()
        )
    }
}

val domainModule = listOf(useCaseModule)