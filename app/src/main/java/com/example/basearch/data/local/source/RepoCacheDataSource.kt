package com.example.basearch.data.local.source

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.local.model.RepoCache
import com.example.basearch.data.remote.BaseErrorData

interface RepoCacheDataSource {
    suspend fun getRepos(): ResultWrapper<List<RepoCache>?, BaseErrorData<Void>>
    suspend fun getRepo(id: Int): ResultWrapper<RepoCache?, BaseErrorData<Void>>
    suspend fun saveRepo(repoCache: RepoCache)
    suspend fun saveAllRepos(repoCaches: List<RepoCache>)
}