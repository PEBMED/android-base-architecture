package com.example.basearch.domain.usecases

import com.example.basearch.data.ResultWrapper
import com.example.basearch.data.local.preferences.SharedPreferencesUtil
import com.example.basearch.domain.entities.Repo
import com.example.basearch.domain.repository.RepoRepository
import com.example.basearch.presentation.extensions.getCurrentDateTime
import com.example.basearch.presentation.extensions.toCacheFormat
import com.example.basearch.presentation.ui.ViewStateResource

class GetReposUseCase(
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val repoRepository: RepoRepository
) : BaseUseCase<List<Repo>, GetReposUseCase.Params>() {

    override suspend fun run(params: Params): ViewStateResource<List<Repo>> {
        return if (params.forceSync) {
            getAllFromRemote()
        } else {
            when (val localResult = repoRepository.getAllLocalRepos()) {
                is ResultWrapper.Success -> {
                    if (localResult.data != null && localResult.data.isNotEmpty())
                        ViewStateResource.Success(localResult.data)
                    else
                        ViewStateResource.Empty()
                }

                is ResultWrapper.Error -> {
                    ViewStateResource.Error(localResult.data?.errorMessage)
                }
            }
        }
    }

    private suspend fun getAllFromRemote(): ViewStateResource<List<Repo>> {
        return when (val remoteResult = repoRepository.getAllRemoteRepos(1, "kotlin")) {
            is ResultWrapper.Success -> {
                if (remoteResult.data.isNotEmpty()) {
                    sharedPreferencesUtil.lastRepoSyncDate = getCurrentDateTime().toCacheFormat()

                    ViewStateResource.Success(remoteResult.data)
                } else
                    ViewStateResource.Empty()
            }

            is ResultWrapper.Error -> {
                ViewStateResource.Error(remoteResult.data?.errorMessage)
            }
        }
    }

    data class Params(val forceSync: Boolean)
}