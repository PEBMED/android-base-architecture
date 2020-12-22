package com.example.basearch.presentation.ui.activity

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.IdlingRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.pebmed.domain.MockPullRequestModel
import br.com.pebmed.domain.FakeResultWrapper
import br.com.pebmed.domain.MockUserModel
import br.com.pebmed.domain.usecases.GetPullRequestUseCase
import com.example.basearch.R
import com.example.basearch.presentation.ui.pullRequest.details.PullRequestActivity
import com.example.basearch.presentation.ui.pullRequest.details.PullRequestViewModel
import com.pebmed.basearch.presentation.utils.GlobalEspressoIdlingResource
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
    private lateinit var getPullRequestUseCase: GetPullRequestUseCase

    private lateinit var pullRequestViewModel: PullRequestViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)

        pullRequestViewModel = PullRequestViewModel(getPullRequestUseCase)

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
        val pullRequestModel = MockPullRequestModel.mock(
            MockUserModel.mock()
        )
        coEvery {
            getPullRequestUseCase.runAsync(any())
        } returns FakeResultWrapper.mockSuccess(pullRequestModel)

        val intent =
            Intent(ApplicationProvider.getApplicationContext(), PullRequestActivity::class.java)
        intent.putExtra("owner", "TEST_OWNER")
        intent.putExtra("repoName", "TEST_REPO")
        intent.putExtra("pullRequestNumber", 1000L)

        launchActivity<PullRequestActivity>(intent).use { scenario ->
            assertDisplayed(R.id.textAuthorName, pullRequestModel.user.login)
        }
    }

    //TODO test error State

    //TODO test loading State (probably will need to make an Custom IdlingResource)
}