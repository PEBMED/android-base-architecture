package br.com.pebmed.domain

import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

class MockGetPullRequestUseCase(val mock: GetPullRequestUseCase) {

    fun mockSuccess() {
        coEvery {
            mock.runAsync(any())
        } returns ResultWrapper(success = MockPullRequestModel.mock(MockUserModel.mock()))
    }

    fun mockUiSuccess() {
        coEvery {
            mock.runAsync(any())
        } returns ResultWrapper(success = MockPullRequestModel.mockUiModel(MockUserModel.mock()))
    }

}