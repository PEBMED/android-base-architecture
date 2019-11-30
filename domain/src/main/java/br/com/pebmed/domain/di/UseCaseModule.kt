package br.com.pebmed.domain.di

import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import br.com.pebmed.domain.usecases.GetReposUseCase
import br.com.pebmed.domain.usecases.ListPullRequestsUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        GetReposUseCase(
            repoRepository = get()
        )
    }

    factory {
        ListPullRequestsUseCase(
            pullRequestRepository = get()
        )
    }

    factory {
        GetPullRequestUseCase(
            pullRequestRepository = get()
        )
    }
}