package br.com.pebmed.data.di

import br.com.pebmed.data.remote.source.PullRequestRemoteDataSource
import br.com.pebmed.data.remote.source.PullRequestRemoteDataSourceImpl
import br.com.pebmed.data.remote.source.RepoRemoteDataSouce
import br.com.pebmed.data.remote.source.RepoRemoteDataSourceImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDataSourceModule = module {

    factory {
        RepoRemoteDataSourceImpl(
            repoApi = get()
        )
    } bind RepoRemoteDataSouce::class

    factory {
        PullRequestRemoteDataSourceImpl(
            pullRequestApi = get()
        )
    } bind PullRequestRemoteDataSource::class
}