package br.com.pebmed.data.di

import androidx.room.Room
import br.com.pebmed.data.local.database.WBDatabase
import com.example.basearch.data.local.preferences.SharedPreferencesUtil
import br.com.pebmed.data.local.source.RepoCacheDataSource
import br.com.pebmed.data.local.source.RepoCacheDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val cacheDataSourceModule = module {
    single { SharedPreferencesUtil(androidContext()) }
    single { Room.databaseBuilder(androidContext(), WBDatabase::class.java, "basearch.db").build() }
    single { get<WBDatabase>().repoDao() }

    factory { RepoCacheDataSourceImpl(repoDao = get()) } bind RepoCacheDataSource::class
}