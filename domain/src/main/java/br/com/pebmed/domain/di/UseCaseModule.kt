package br.com.pebmed.domain.di

import br.com.pebmed.domain.usecases.*
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

    factory {
        GetPlansUseCase()
    }

    factory {
        ValidatePurchasedPlanUseCase()
    }
}