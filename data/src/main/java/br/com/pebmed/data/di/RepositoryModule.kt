package br.com.pebmed.data.di

import br.com.pebmed.data.pullRequest.PullRequestRepositoryImpl
import br.com.pebmed.data.repo.RepoRepositoryImpl
import br.com.pebmed.domain.repository.PullRequestRepository
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

    factory<PullRequestRepository> {
        PullRequestRepositoryImpl(
            pullRequestRemoteDataSource = get()
        )
    }
}