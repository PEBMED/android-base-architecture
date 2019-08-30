package br.com.pebmed.data.local.source

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.data.local.database.RepoDao
import br.com.pebmed.data.local.model.RepoCache
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.data.repository.BaseDataSourceImpl
import br.com.pebmed.data.repository.ExecuteAsync

class RepoCacheDataSourceImpl(private val repoDao: RepoDao) : RepoCacheDataSource,
    BaseDataSourceImpl() {
    override suspend fun getRepos(): ResultWrapper<List<RepoCache>?, BaseErrorData<Void>> {
        return safeCall(object : ExecuteAsync<List<RepoCache>?> {
            override suspend fun execute(): List<RepoCache>? {
                return repoDao.getAll()
            }
        })
    }

    override suspend fun getRepo(id: Int): ResultWrapper<RepoCache?, BaseErrorData<Void>> {
        return safeCall(object : ExecuteAsync<RepoCache?> {
            override suspend fun execute(): RepoCache? {
                return repoDao.getFromId(id)
            }
        })
    }

    override suspend fun saveRepo(repoCache: RepoCache) {
        repoDao.upsert(repoCache)
    }

    override suspend fun saveAllRepos(repoCaches: List<RepoCache>) {
        repoDao.upsert(repoCaches)
    }
}