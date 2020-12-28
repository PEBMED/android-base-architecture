package com.pebmed.basearch.presentation.di

import br.com.pebmed.domain.usecases.*
import com.pebmed.basearch.presentation.ui.billing.BillingViewModel
import com.pebmed.basearch.presentation.ui.main.MainViewModel
import com.pebmed.basearch.presentation.ui.pullRequest.list.PullRequestListViewModel
import com.pebmed.basearch.presentation.ui.pullRequest.details.PullRequestViewModel
import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(
            get() as GetReposUseCase
        )
    }

    viewModel {
        PullRequestListViewModel(
            Dispatchers.IO,
            get() as GetPullRequestsUseCase
        )
    }

    viewModel {
        PullRequestViewModel(
            Dispatchers.IO,
            get() as GetPullRequestUseCase
        )
    }

    viewModel {
        BillingViewModel(
            get() as GooglePlayBillingClientWrapper,
            get() as GetPlansUseCase,
            get() as ValidatePurchasedStorePlanUseCase,
            get() as SavePurchasedPlanUseCase,
            get() as GetPurchasedPlanUseCase,
            get() as SetPendingSubscriptionValidationUseCase,
            get() as GetPendingSubscriptionValidationUseCase
        )
    }
}