package br.com.pebmed.data.repo.local

import br.com.pebmed.data.repo.local.model.RepoEntityModel
import br.com.pebmed.data.base.BaseDataSourceImpl
import br.com.pebmed.data.repo.local.RepoDao
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper

class RepoLocalDataSource(private val repoDao: RepoDao) : BaseDataSourceImpl() {
    suspend fun getRepos(): ResultWrapper<List<RepoEntityModel>, BaseErrorData<Unit>> {
        return safeCall { repoDao.getAll() }
    }

    suspend fun getRepo(id: Int): ResultWrapper<RepoEntityModel?, BaseErrorData<Unit>> {
        return safeCall { repoDao.getFromId(id) }
    }

    suspend fun saveRepo(repo: RepoEntityModel) {
        repoDao.upsert(repo)
    }

    suspend fun saveAllRepos(repos: List<RepoEntityModel>) {
        repoDao.upsert(repos)
    }
}