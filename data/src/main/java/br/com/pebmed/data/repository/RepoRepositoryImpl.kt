package br.com.pebmed.data.repository

import br.com.pebmed.data.local.model.mapToRepo
import br.com.pebmed.data.local.source.RepoCacheDataSourceImpl
import br.com.pebmed.data.remote.model.mapToRepo
import br.com.pebmed.data.remote.source.RepoRemoteDataSourceImpl
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GetReposErrorData
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.repository.RepoRepository
import com.example.basearch.data.local.preferences.SharedPreferencesUtil

class RepoRepositoryImpl(
    private val remoteRepository: RepoRemoteDataSourceImpl,
    private val localRepository: RepoCacheDataSourceImpl,
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
    ): ResultWrapper<List<Repo>, BaseErrorData<GetReposErrorData>> {
        val remoteResult = remoteRepository.getRepos(page, language)

        return remoteResult.transformSuccess { obj ->
            obj.repoPayloads.map {
                it.mapToRepo()
            }
        }
    }

    override suspend fun getAllLocalRepos(): ResultWrapper<List<Repo>?, BaseErrorData<Unit>> {
        val localResponse = localRepository.getRepos()

        return localResponse.transformSuccess { obj -> obj?.map { it.mapToRepo() } }
    }

    override suspend fun getAllRepos(
        fromRemote: Boolean,
        page: Int,
        language: String
    ): ResultWrapper<List<Repo>?, BaseErrorData<GetReposErrorData>> {
        return if (fromRemote) {
            getAllRemoteRepos(page, language)
        } else {
            val localResult = getAllLocalRepos()

            localResult.transformError { obj ->
                BaseErrorData<GetReposErrorData>(
                    null,
                    obj?.errorMessage
                )
            }
        }
    }

    override suspend fun getRepo(
        id: Int,
        fromRemote: Boolean
    ): ResultWrapper<Repo?, BaseErrorData<GetReposErrorData>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}