package com.pebmed.basearch

import android.app.Application
import com.pebmed.basearch.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@CustomApplication)
            // modules
            modules(mainModule)
        }
    }
}