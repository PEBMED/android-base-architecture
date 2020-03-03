package br.com.pebmed.data.di

import androidx.room.Room
import br.com.pebmed.data.base.WBDatabase
import br.com.pebmed.data.base.SharedPreferencesUtil
import br.com.pebmed.data.repo.local.RepoLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {
    single { SharedPreferencesUtil(androidContext()) }
    single { Room.databaseBuilder(androidContext(), WBDatabase::class.java, "basearch.db").build() }
    single { get<WBDatabase>().repoDao() }

    factory { RepoLocalDataSource(repoDao = get()) }
}