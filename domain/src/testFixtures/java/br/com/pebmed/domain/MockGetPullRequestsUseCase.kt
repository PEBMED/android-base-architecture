package br.com.pebmed.domain

import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.usecases.GetPullRequestsUseCase
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

class MockGetPullRequestsUseCase(val mock: GetPullRequestsUseCase) {

    fun mockSuccess() {
        coEvery {
            mock.runAsync()
        } returns ResultWrapper(success = MockPullRequestModel.mockList())
    }

    companion object {
        fun mockParams() = GetPullRequestsUseCase.Params(
            owner = "OwnerModel",
            repoName = "RepoName"
        )
    }
}