package br.com.pebmed.domain.repository

import br.com.pebmed.domain.MockGitRepoModel
import br.com.pebmed.domain.base.ResultWrapper
import io.mockk.coEvery

class MockRepoRepository(val mock: RepoRepository) {

    fun mockGetAllReposSuccessWithOneListItem() {
        coEvery {
            mock.getAllRepos(
                    fromRemote = true,
                    page = 1,
                    language = "java"
            )
        } returns ResultWrapper(
                success = MockGitRepoModel.mockListWithOneGenericItem()
        )
    }

}