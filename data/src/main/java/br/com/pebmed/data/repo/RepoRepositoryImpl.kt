package br.com.pebmed.data.repo

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.repository.RepoRepository
import br.com.pebmed.data.base.SharedPreferencesUtil
import br.com.pebmed.data.repo.local.RepoLocalDataSource
import br.com.pebmed.data.repo.remote.RepoRemoteDataSource

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
        language: String
    ): ResultWrapper<List<RepoModel>, BaseErrorData<Unit>> {
        val remoteResult = remoteRepository.getRepos(page, language)

        return remoteResult.transformSuccess { getReposResponse ->
            getReposResponse.repos.map { repoPayload ->
                repoPayload.mapTo()
            }
        }
    }

    override suspend fun getAllLocalRepos(): ResultWrapper<List<RepoModel>, BaseErrorData<Unit>> {
        val localResponse = localRepository.getRepos()

        return localResponse.transformSuccess { repoEntities ->
            repoEntities.map { repoEntity ->
                repoEntity.mapTo()
            }
        }
    }

    override suspend fun getAllRepos(
        fromRemote: Boolean,
        page: Int,
        language: String
    ): ResultWrapper<List<RepoModel>, BaseErrorData<Unit>> {
        return if (fromRemote) {
            getAllRemoteRepos(page, language)
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