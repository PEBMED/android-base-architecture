package com.pebmed.basearch.presentation.di

import com.pebmed.basearch.presentation.ui.billing.BillingViewModel
import com.pebmed.basearch.presentation.ui.main.MainViewModel
import com.pebmed.basearch.presentation.ui.pullRequest.list.PullRequestListViewModel
import com.pebmed.basearch.presentation.ui.pullRequest.details.PullRequestViewModel
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
            getPullRequestsUseCase = get()
        )
    }

    viewModel {
        PullRequestViewModel(
            getPullRequestUseCase = get()
        )
    }

    viewModel {
        BillingViewModel(
            googlePlayBillingClientWrapper = get(),
            getPlansUseCase = get(),
            validatePurchasedStorePlanUseCase = get(),
            savePurchasedPlanUseCase = get(),
            getPurchasedPlanUseCase = get(),
            setPendingSubscriptionValidationUseCase = get(),
            getPendingSubscriptionValidationUseCase = get()
        )
    }
}