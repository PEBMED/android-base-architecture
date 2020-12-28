package com.pebmed.platform.di

import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val billingModule = module {
    single {
        GooglePlayBillingClientWrapper(androidContext())
    }
}