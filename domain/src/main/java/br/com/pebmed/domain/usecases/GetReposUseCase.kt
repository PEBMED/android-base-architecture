package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.ViewStateResource
import br.com.pebmed.domain.extensions.getCurrentDateTime
import br.com.pebmed.domain.extensions.toCacheFormat
import com.example.basearch.data.ResultWrapper
import com.example.basearch.domain.entities.Repo
import com.example.basearch.domain.repository.RepoRepository

class GetReposUseCase(
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
                    repoRepository.saveLastSyncDate(getCurrentDateTime().toCacheFormat())

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