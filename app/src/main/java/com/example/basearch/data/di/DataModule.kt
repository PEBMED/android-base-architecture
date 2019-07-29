package com.example.basearch.data.di

import com.example.basearch.data.repository.RepoRepositoryImpl
import com.example.basearch.domain.repository.RepoRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<RepoRepository> {
        RepoRepositoryImpl(
            remoteRepository = get(),
            localRepository = get(),
            sharedPreferencesUtil = get()
        )
    }
}

val dataModules = listOf(remoteDataSourceModule, cacheDataModule, repositoryModule)