package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.repository.MockRepoRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
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
        mockRepoRepository.mockGetAllReposSuccessWithOneListItemAndPaginationNull()
        mockRepoRepository.mockSaveLastSyncDateSuccess()
        val fakePage = 1
        val fakePerPage = 5
        val fakeLanguage = "java"

        //ACT
        runBlocking {
            getReposUseCase.runAsync(GetReposUseCase.Params(true, fakePage))

            //ASSERT
            coVerify {
                mockRepoRepository.mock.getAllRepos(
                    fromRemote = true,
                    page = fakePage,
                    perPage = fakePerPage,
                    language = fakeLanguage
                )
                mockRepoRepository.mock.saveLastSyncDate(any())
            }

            confirmVerified(mockRepoRepository.mock)
        }
    }

    @Test
    fun `SHOULD not save last sync date WHEN result success is empty`() {
        //ARRANGE
        mockRepoRepository.mockGetAllReposSuccessWithResultEmpty()
        val fakePage = 1
        val fakePerPage = 5
        val fakeLanguage = "java"

        //ACT
        runBlocking {
            getReposUseCase.runAsync(GetReposUseCase.Params(true, fakePage))

            //ASSERT
            coVerify {
                mockRepoRepository.mock.getAllRepos(
                    fromRemote = true,
                    page = fakePage,
                    perPage = fakePerPage,
                    language = fakeLanguage
                )
            }
            verify (exactly = 0) {mockRepoRepository.mock.saveLastSyncDate(any())}

            confirmVerified(mockRepoRepository.mock)
        }
    }

    @Test
    fun `SHOULD not save last sync date WHEN force sync param is false`() {
        //ARRANGE
        mockRepoRepository.mockGetAllReposSuccessWithOneListItemAndForceSyncFalse()
        val fakePage = 1
        val fakePerPage = 5
        val fakeLanguage = "java"

        //ACT
        runBlocking {
            getReposUseCase.runAsync(GetReposUseCase.Params(false, fakePage))

            //ASSERT
            coVerify {
                mockRepoRepository.mock.getAllRepos(
                    fromRemote = false,
                    page = fakePage,
                    perPage = fakePerPage,
                    language = fakeLanguage
                )
            }
            verify (exactly = 0) {mockRepoRepository.mock.saveLastSyncDate(any())}

            confirmVerified(mockRepoRepository.mock)
        }
    }
}