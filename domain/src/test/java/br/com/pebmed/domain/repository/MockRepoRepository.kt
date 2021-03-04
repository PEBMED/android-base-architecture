package br.com.pebmed.domain.repository

import br.com.pebmed.domain.MockGitRepoModel
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.RepoListModel
import io.mockk.coEvery

class MockRepoRepository(val mock: RepoRepository) {

    fun mockGetAllReposSuccessWithOneListItemAndPaginationNull() {
        coEvery {
            mock.getAllRepos(
                    fromRemote = true,
                    page = 1,
                    perPage = 5,
                    language = "java"
            )
        } returns ResultWrapper(
                success = RepoListModel(MockGitRepoModel.mockListWithOneGenericItem())
        )
    }

    fun mockGetAllReposSuccessWithEmptyResult() {
        coEvery {
            mock.getAllRepos(
                fromRemote = true,
                page = 1,
                perPage = 5,
                language = "java"
            )
        } returns ResultWrapper(
            success = RepoListModel(emptyList())
        )
    }

    fun mockGetAllReposSuccessWithOneListItemAndForceSyncFalse() {
        coEvery {
            mock.getAllRepos(
                fromRemote = false,
                page = 1,
                perPage = 5,
                language = "java"
            )
        } returns ResultWrapper(
            success = RepoListModel(MockGitRepoModel.mockListWithOneGenericItem())
        )
    }

    fun mockSaveLastSyncDateSuccess() {
        coEvery {
            mock.saveLastSyncDate(any())
        } returns Unit
    }

}