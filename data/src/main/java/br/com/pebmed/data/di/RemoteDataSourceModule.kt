package br.com.pebmed.data.di

import br.com.pebmed.data.pullRequest.PullRequestRemoteDataSource
import br.com.pebmed.data.repo.remote.RepoRemoteDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {
    factory {
        RepoRemoteDataSource(
            repoApi = get()
        )
    }

    factory {
        PullRequestRemoteDataSource(
            pullRequestApi = get()
        )
    }
}