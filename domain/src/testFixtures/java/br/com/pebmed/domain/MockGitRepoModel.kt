package br.com.pebmed.domain

import br.com.pebmed.domain.entities.RepoModel
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

object MockGitRepoModel {
    fun mockListWithOneGenericItem(): List<RepoModel>  {
        return generic(1)
    }

    private fun generic(itemsOnList: Int): List<RepoModel> {
        val list = mutableListOf<RepoModel>()

        for (index in 0 .. itemsOnList) {
            list.add(
                generic()
            )
        }

        return list
    }

    fun generic() : RepoModel {
        val mockedRepoModel = mockk<RepoModel>()

        every {
            mockedRepoModel.id
        } returns 1

        every {
            mockedRepoModel.ownerModel
        } returns MockOwnerModel.mock()

        return mockedRepoModel
    }
}