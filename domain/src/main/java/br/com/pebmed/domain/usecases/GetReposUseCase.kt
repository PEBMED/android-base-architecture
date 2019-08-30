package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.extensions.getCurrentDateTime
import br.com.pebmed.domain.extensions.toCacheFormat
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.repository.RepoRepository

class GetReposUseCase(
    private val repoRepository: RepoRepository
) : BaseUseCase<ResultWrapper<List<Repo>?, String>, GetReposUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<List<Repo>?, String> {
        return if (params.forceSync) {
            getAllFromRemote()
        } else {
            when (val localResult = repoRepository.getAllLocalRepos()) {
                is ResultWrapper.Success -> {
                    ResultWrapper.Success(localResult.data)
                }

                is ResultWrapper.Error -> {
                    ResultWrapper.Error(localResult.data?.errorMessage)
                }
            }
        }
    }

    private suspend fun getAllFromRemote(): ResultWrapper<List<Repo>?, String> {
        return when (val remoteResult = repoRepository.getAllRemoteRepos(1, "kotlin")) {
            is ResultWrapper.Success -> {
                if (remoteResult.data.isNotEmpty()) {
                    repoRepository.saveLastSyncDate(getCurrentDateTime().toCacheFormat())
                }

                ResultWrapper.Success(remoteResult.data)
            }

            is ResultWrapper.Error -> {
                ResultWrapper.Error(remoteResult.data?.errorMessage)
            }
        }
    }

    data class Params(val forceSync: Boolean)
}