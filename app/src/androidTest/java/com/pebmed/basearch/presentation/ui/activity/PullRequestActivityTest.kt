package com.pebmed.basearch.presentation.ui.activity

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.IdlingRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.pebmed.domain.MockGetPullRequestUseCase
import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.MockUserModel
import com.pebmed.basearch.R
import com.pebmed.basearch.presentation.ui.pullRequest.details.PullRequestActivity
import com.pebmed.basearch.presentation.ui.pullRequest.details.PullRequestViewModel
import com.pebmed.basearch.presentation.utils.GlobalEspressoIdlingResource
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4ClassRunner::class)
class PullRequestActivityTest {

    @MockK
    private lateinit var mockGetPullRequestUseCase: MockGetPullRequestUseCase

    private lateinit var pullRequestViewModel: PullRequestViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)

        mockGetPullRequestUseCase = MockGetPullRequestUseCase(mockk())

        pullRequestViewModel = PullRequestViewModel(
            Dispatchers.Main,
            mockGetPullRequestUseCase.mock
        )

        loadKoinModules(module {
            viewModel(override = true) {
                pullRequestViewModel
            }
        })

        IdlingRegistry.getInstance().register(
            GlobalEspressoIdlingResource.countingIdlingResource
        )
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(
            GlobalEspressoIdlingResource.countingIdlingResource
        )
    }

    @Test
    fun openActivity_SuccessLoad() {
        val pullRequestModel = MockPullRequestModel.mockUiModel(
            MockUserModel.generic()
        )

        mockGetPullRequestUseCase.mockUiSuccess()

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), PullRequestActivity::class.java)
        intent.putExtra("owner", "TEST_OWNER")
        intent.putExtra("repoName", "TEST_REPO")
        intent.putExtra("pullRequestNumber", 1000L)

        launchActivity<PullRequestActivity>(intent).use { scenario ->
            assertDisplayed(R.id.textAuthorName, pullRequestModel.user.login)
        }
    }
}