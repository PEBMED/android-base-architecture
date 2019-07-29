package com.example.basearch.domain.repository

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.remote.BaseErrorData
import com.example.basearch.domain.entities.Repo

interface RepoRepository {
    fun getLastSyncDate(): String
    suspend fun getAllRemoteRepos(page: Int, language: String): ResultWrapper<List<Repo>, BaseErrorData<Void>>
    suspend fun getAllLocalRepos(): ResultWrapper<List<Repo>?, BaseErrorData<Void>>
    suspend fun getRepo(id: Int, fromRemote: Boolean = false): ResultWrapper<Repo?, BaseErrorData<Void>>
}