package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.GetReposErrorData
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.extensions.getCurrentDateTime
import br.com.pebmed.domain.extensions.toCacheFormat
import br.com.pebmed.domain.repository.RepoRepository

class GetReposUseCase(
    private val repoRepository: RepoRepository
) : BaseUseCase<ResultWrapper<List<Repo>?, BaseErrorData<GetReposErrorData>?>, GetReposUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<List<Repo>?, BaseErrorData<GetReposErrorData>?> {
        val result = repoRepository.getAllRepos(params.forceSync, 1, "kotlin")

        if (result.success != null && params.forceSync) {
            repoRepository.saveLastSyncDate(getCurrentDateTime().toCacheFormat())
        }

        return result
    }

    data class Params(val forceSync: Boolean)
}