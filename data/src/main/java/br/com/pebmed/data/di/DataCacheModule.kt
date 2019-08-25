package com.example.basearch.data.di

import androidx.room.Room
import com.example.basearch.data.local.database.WBDatabase
import com.example.basearch.data.local.preferences.SharedPreferencesUtil
import com.example.basearch.data.local.source.RepoCacheDataSource
import com.example.basearch.data.local.source.RepoCacheDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val cacheDataModule = module {
    single { SharedPreferencesUtil(androidContext()) }
    single { Room.databaseBuilder(androidContext(), WBDatabase::class.java, "basearch.db").build() }
    single { get<WBDatabase>().repoDao() }

    factory { RepoCacheDataSourceImpl(repoDao = get()) } bind RepoCacheDataSource::class
//    factory { RepoCacheDataSourceImpl(repoDao = get()) }
//    factory<RepoCacheDataSource> { RepoCacheDataSourceImpl(repoDao = get()) }
}