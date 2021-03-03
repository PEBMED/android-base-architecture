package br.com.pebmed.domain.repository

import br.com.pebmed.domain.MockGitRepoModel
import br.com.pebmed.domain.base.ResultWrapper
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
                success = Pair(MockGitRepoModel.mockListWithOneGenericItem(), null)
        )
    }

    fun mockGetAllReposSuccessWithResultEmpty() {
        coEvery {
            mock.getAllRepos(
                fromRemote = true,
                page = 1,
                perPage = 5,
                language = "java"
            )
        } returns ResultWrapper(
            success = Pair(emptyList(), null)
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
            success = Pair(MockGitRepoModel.mockListWithOneGenericItem(), null)
        )
    }

    fun mockSaveLastSyncDateSuccess() {
        coEvery {
            mock.saveLastSyncDate(any())
        } returns Unit
    }

}