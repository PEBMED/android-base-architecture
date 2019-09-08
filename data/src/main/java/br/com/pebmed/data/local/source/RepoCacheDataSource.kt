package br.com.pebmed.data.local.source

import br.com.pebmed.data.local.model.RepoCache
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper

interface RepoCacheDataSource {
    suspend fun getRepos(): ResultWrapper<List<RepoCache>?, BaseErrorData<Unit>>
    suspend fun getRepo(id: Int): ResultWrapper<RepoCache?, BaseErrorData<Unit>>
    suspend fun saveRepo(repoCache: RepoCache)
    suspend fun saveAllRepos(repoCaches: List<RepoCache>)
}