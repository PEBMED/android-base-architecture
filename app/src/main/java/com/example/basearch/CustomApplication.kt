package com.example.basearch

import android.app.Application
import com.example.basearch.data.di.dataModules
import com.example.basearch.domain.di.domainModule
import com.example.basearch.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@CustomApplication)
            // modules
            modules((dataModules + domainModule + listOf(presentationModule)))
        }
    }
}