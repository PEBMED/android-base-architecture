package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.FakeGitRepoModel
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.repository.RepoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetReposUseCaseTest {
    @MockK(relaxUnitFun = true)
    private lateinit var repoRepository: RepoRepository

    private val fakePage = 1
    private val fakeLanguage = "java"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `SHOULD save last sync date WHEN force sync param is true`() = runBlocking {
        coEvery {
            repoRepository.getAllRepos(
                fromRemote = true,
                page = fakePage,
                language = fakeLanguage
            )
        } returns ResultWrapper(
            success = FakeGitRepoModel.mock(1)
        )

        GetReposUseCase(repoRepository).runAsync(GetReposUseCase.Params(true))

        coVerify {
            repoRepository.getAllRepos(
                fromRemote = true,
                page = fakePage,
                language = fakeLanguage
            )
            repoRepository.saveLastSyncDate(any())
        }

        confirmVerified(repoRepository)
    }
}