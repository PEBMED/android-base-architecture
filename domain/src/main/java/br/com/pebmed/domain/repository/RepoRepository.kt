package br.com.pebmed.domain.repository

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.RepoListModel
import br.com.pebmed.domain.entities.RepoModel

interface RepoRepository {
    fun getLastSyncDate(): String

    fun saveLastSyncDate(dateString: String)

    suspend fun getAllRemoteRepos(
        page: Int,
        perPage: Int,
        language: String
    ): ResultWrapper<RepoListModel, BaseErrorData<Unit>>

    suspend fun getAllLocalRepos(): ResultWrapper<RepoListModel, BaseErrorData<Unit>>

    suspend fun getAllRepos(
        fromRemote: Boolean = false,
        page: Int,
        perPage: Int,
        language: String
    ): ResultWrapper<RepoListModel, BaseErrorData<Unit>>

    suspend fun getRepo(
        id: Int,
        fromRemote: Boolean = false
    ): ResultWrapper<RepoModel, BaseErrorData<Unit>>
}