package com.example.basearch

import android.app.Application
import br.com.pebmed.data.di.dataModules
import br.com.pebmed.domain.di.domainModule
import com.example.basearch.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@CustomApplication)
            // modules
            modules((dataModules + domainModule + listOf(viewModelModule)))
        }
    }
}