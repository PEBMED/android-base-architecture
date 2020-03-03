package br.com.pebmed.domain.di

import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import br.com.pebmed.domain.usecases.GetPullRequestsUseCase
import br.com.pebmed.domain.usecases.GetReposUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        GetReposUseCase(
            repoRepository = get()
        )
    }

    factory {
        GetPullRequestsUseCase(
            pullRequestRepository = get()
        )
    }

    factory {
        GetPullRequestUseCase(
            pullRequestRepository = get()
        )
    }
}