package br.com.pebmed.data.di

import br.com.pebmed.data.repository.RepoRepositoryImpl
import br.com.pebmed.domain.repository.RepoRepository
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