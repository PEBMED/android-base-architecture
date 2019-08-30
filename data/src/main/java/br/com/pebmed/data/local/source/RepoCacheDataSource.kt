package br.com.pebmed.data.local.source

import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.data.local.model.RepoCache
import br.com.pebmed.domain.base.BaseErrorData

interface RepoCacheDataSource {
    suspend fun getRepos(): ResultWrapper<List<RepoCache>?, BaseErrorData<Void>>
    suspend fun getRepo(id: Int): ResultWrapper<RepoCache?, BaseErrorData<Void>>
    suspend fun saveRepo(repoCache: RepoCache)
    suspend fun saveAllRepos(repoCaches: List<RepoCache>)
}