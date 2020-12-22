package com.pebmed.platform.di

import com.pebmed.platform.network.NetworkConnectivityManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single {
        NetworkConnectivityManager(androidContext())
    }
}