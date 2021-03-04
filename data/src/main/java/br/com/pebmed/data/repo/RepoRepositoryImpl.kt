package br.com.pebmed.data.repo

import br.com.pebmed.data.base.SharedPreferencesUtil
import br.com.pebmed.data.repo.local.RepoLocalDataSource
import br.com.pebmed.data.repo.remote.RepoRemoteDataSource
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.RepoListModel
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.repository.RepoRepository

class RepoRepositoryImpl(
    private val remoteRepository: RepoRemoteDataSource,
    private val localRepository: RepoLocalDataSource,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : RepoRepository {
    override fun getLastSyncDate(): String {
        return sharedPreferencesUtil.lastRepoSyncDate
    }

    override fun saveLastSyncDate(dateString: String) {
        sharedPreferencesUtil.lastRepoSyncDate = dateString
    }

    override suspend fun getAllRemoteRepos(
        page: Int,
        perPage: Int,
        language: String
    ): ResultWrapper<RepoListModel, BaseErrorData<Unit>> {
        val remoteResult = remoteRepository.getRepos(page, perPage, language)

        val totalReposCount = remoteResult.success?.totalCount
        val totalReposLoaded = page * perPage
        val hasNextPage = compareValues(totalReposCount, totalReposLoaded) > 0
        val nextPage =
            if (hasNextPage) {
                page + 1
            } else {
                0
            }

        return remoteResult.transformSuccess { getReposResponse ->
            RepoListModel(
                getReposResponse.repos.map { repoPayload ->
                    repoPayload.mapTo()
                },
                hasNextPage,
                nextPage
            )
        }

    }

    override suspend fun getAllLocalRepos(): ResultWrapper<RepoListModel, BaseErrorData<Unit>> {
        val localResponse = localRepository.getRepos()

        return localResponse.transformSuccess { repoEntities ->
            RepoListModel(
                listOfRepoModel = repoEntities.map { repoEntity ->
                    repoEntity.mapTo()
                }
            )
        }
    }

    override suspend fun getAllRepos(
        fromRemote: Boolean,
        page: Int,
        perPage: Int,
        language: String
    ): ResultWrapper<RepoListModel, BaseErrorData<Unit>> {
        return if (fromRemote) {
            getAllRemoteRepos(page, perPage, language)
        } else {
            getAllLocalRepos()
        }
    }

    override suspend fun getRepo(
        id: Int,
        fromRemote: Boolean
    ): ResultWrapper<RepoModel, BaseErrorData<Unit>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}