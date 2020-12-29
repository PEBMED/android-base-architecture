package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.repository.MockRepoRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetReposUseCaseTest {
    private lateinit var mockRepoRepository: MockRepoRepository

    private lateinit var getReposUseCase: GetReposUseCase

    @Before
    fun setUp() {
        mockRepoRepository = MockRepoRepository(mockk())

        getReposUseCase = GetReposUseCase(mockRepoRepository.mock)
    }

    @Test
    fun `SHOULD save last sync date WHEN force sync param is true`() {
        //ARRANGE
        mockRepoRepository.mockGetAllReposSuccessWithOneListItem()
        val fakePage = 1
        val fakeLanguage = "java"

        //ACT
        runBlocking {
            getReposUseCase.runAsync(GetReposUseCase.Params(true))

            //ASSERT
            coVerify {
                mockRepoRepository.mock.getAllRepos(
                    fromRemote = true,
                    page = fakePage,
                    language = fakeLanguage
                )
                mockRepoRepository.mock.saveLastSyncDate(any())
            }

            confirmVerified(mockRepoRepository.mock)
        }
    }
}