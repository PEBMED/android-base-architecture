package br.com.pebmed.domain

import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.UserModel
import java.util.*
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

object MockPullRequestModel {

    val mockDate = Date()

    fun mockListWithOneGenericItem() : List<PullRequestModel> {
        return mockListWithGenericItems(1)
    }

    fun mockEmptyList() : List<PullRequestModel> {
        return mockListWithGenericItems(0)
    }

    private fun mockListWithGenericItems(itemsOnList: Int) : List<PullRequestModel> {
        val list = mutableListOf<PullRequestModel>()

        for (index in 1 .. itemsOnList) {
            list.add(generic())
        }

        return list
    }

    fun generic() : PullRequestModel {
        val mockedPullRequestModel = mockk<PullRequestModel>()

        every {
            mockedPullRequestModel.number
        } returns 1

        every {
            mockedPullRequestModel.htmlUrl
        } returns "http://the.url"

        every {
            mockedPullRequestModel.title
        } returns "Title"

        every {
            mockedPullRequestModel.user
        } returns MockUserModel.generic()

        every {
            mockedPullRequestModel.body
        } returns "Body"

        every {
            mockedPullRequestModel.createdAt
        } returns mockDate

        every {
            mockedPullRequestModel.comments
        } returns 1

        every {
            mockedPullRequestModel.commits
        } returns 1

        every {
            mockedPullRequestModel.additions
        } returns 1

        every {
            mockedPullRequestModel.deletions
        } returns 1

        every {
            mockedPullRequestModel.changedFiles
        } returns 1

        return mockedPullRequestModel
    }

    /*
    * Método específico para o teste de UI
    * Bug no Mockk não permite mockar um Date() por isso
    * é retornado um objeto real nesse caso.
    * Referência: https://github.com/mockk/mockk/issues/253
    * */
    fun mockUiModel(mockUserModel: UserModel) = PullRequestModel(
            number = 1,
            htmlUrl = "htmlUrl",
            title = "title",
            user = mockUserModel,
            body = "body",
            createdAt = MockDate.today(),
            comments = 1,
            commits = 1,
            additions = 1,
            deletions = 1,
            changedFiles = 1
    )
}