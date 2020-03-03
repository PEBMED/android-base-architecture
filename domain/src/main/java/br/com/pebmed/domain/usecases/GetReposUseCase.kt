package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.base.usecase.BaseAsyncUseCase
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.extensions.getCurrentDateTime
import br.com.pebmed.domain.extensions.toCacheFormat
import br.com.pebmed.domain.repository.RepoRepository
import br.com.pebmed.domain.usecases.GetReposUseCase.Params

/**
 * @Regra de negócio:
 * 1) Buscar todos os repositórios de acordo com os parâmetros enviados
 * 2) É necessário persistir a ultima data de sincronização caso o resultado seja sucesso e o
 * parâmetro de sincronização esteja true
 */
class GetReposUseCase(
    private val repoRepository: RepoRepository
) : BaseAsyncUseCase<ResultWrapper<List<RepoModel>, BaseErrorData<BaseErrorStatus>>, Params>() {

    override suspend fun runAsync(params: Params): ResultWrapper<List<RepoModel>, BaseErrorData<BaseErrorStatus>> {
        val result = repoRepository.getAllRepos(
            fromRemote = params.forceSync,
            page = 1,
            language = "java"
        )

        if (result.success != null && params.forceSync) {
            repoRepository.saveLastSyncDate(getCurrentDateTime().toCacheFormat())
        }

        return result.transformError { BaseErrorData(errorBody = BaseErrorStatus.DEFAULT_ERROR) }
    }

    data class Params(val forceSync: Boolean)
}