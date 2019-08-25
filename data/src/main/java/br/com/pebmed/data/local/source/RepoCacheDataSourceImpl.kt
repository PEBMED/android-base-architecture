package com.example.basearch.data.local.source

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.local.database.RepoDao
import com.example.basearch.data.local.model.RepoCache
import com.example.basearch.data.remote.BaseErrorData
import com.example.basearch.data.repository.BaseDataSourceImpl
import com.example.basearch.data.repository.ExecuteAsync

class RepoCacheDataSourceImpl(private val repoDao: RepoDao) : RepoCacheDataSource, BaseDataSourceImpl() {
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