package br.com.pebmed.data.local.source

import br.com.pebmed.data.local.database.RepoDao
import br.com.pebmed.data.local.model.RepoCache
import br.com.pebmed.data.repository.BaseDataSourceImpl
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper

class RepoCacheDataSourceImpl(private val repoDao: RepoDao) : RepoCacheDataSource,
    BaseDataSourceImpl() {
    override suspend fun getRepos(): ResultWrapper<List<RepoCache>?, BaseErrorData<Unit>> {
        return safeCall { repoDao.getAll() }
    }

    override suspend fun getRepo(id: Int): ResultWrapper<RepoCache?, BaseErrorData<Unit>> {
        return safeCall { repoDao.getFromId(id) }
    }

    override suspend fun saveRepo(repoCache: RepoCache) {
        repoDao.upsert(repoCache)
    }

    override suspend fun saveAllRepos(repoCaches: List<RepoCache>) {
        repoDao.upsert(repoCaches)
    }
}