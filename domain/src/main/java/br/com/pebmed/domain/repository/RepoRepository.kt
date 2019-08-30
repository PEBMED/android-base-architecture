package br.com.pebmed.domain.repository

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.Repo

interface RepoRepository {
    fun getLastSyncDate(): String
    fun saveLastSyncDate(dateString: String)
    suspend fun getAllRemoteRepos(
        page: Int,
        language: String
    ): ResultWrapper<List<Repo>, BaseErrorData<Void>>

    suspend fun getAllLocalRepos(): ResultWrapper<List<Repo>?, BaseErrorData<Void>>
    suspend fun getRepo(
        id: Int,
        fromRemote: Boolean = false
    ): ResultWrapper<Repo?, BaseErrorData<Void>>
}