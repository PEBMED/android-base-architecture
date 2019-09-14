package com.example.basearch.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.pebmed.domain.entities.PullRequest
import com.example.basearch.R
import com.example.basearch.presentation.extensions.setGone
import com.example.basearch.presentation.extensions.setVisible
import com.example.basearch.presentation.ui.viewmodel.PullRequestListViewModel
import com.example.basearch.presentation.ui.ViewStateResource
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PullRequestListActivity : AppCompatActivity() {

    private val viewModel by viewModel<PullRequestListViewModel>()

    private lateinit var owner: String
    private lateinit var repoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initRecyclerView()
        this.initObservers()
        this.initListeners()

        owner = intent.extras?.getString("owner").toString()
        repoName = intent.extras?.getString("repoName").toString()

        viewModel.loadPullRequestList(
            owner = owner,
            repoName = repoName
        )
    }

    private fun initObservers() {
        viewModel.pullRequestListState.observe(this, Observer {
            when (it) {
                is ViewStateResource.Loading -> {
                    showLoadingView()
                }

                is ViewStateResource.Success -> {
                    showContent(it.data!!)
                }

                is ViewStateResource.Empty -> {
                    val message = getString(R.string.empty_list)
                }

                is ViewStateResource.Error -> {
                    Log.e("", "Show error")
                }
            }
        })
    }

    private fun initListeners() {
        buttonReposTryAgain.setOnClickListener {
            viewModel.loadPullRequestList(
                owner = "",
                repoName = ""
            )
        }
    }

    private fun initRecyclerView() {
        // Nothing to do for now
    }

    //region ViewStates
    private fun showLoadingView() {
        hideErrorView()
        hideContent()

        layoutReposLoading.setVisible()
    }

    private fun hideLoadingView() {
        layoutReposLoading.setGone()
    }
    private fun showErrorView(message: String) {
        hideLoadingView()
        hideContent()

        textReposError.text = message
        layoutReposError.setVisible()
    }

    private fun hideErrorView() {
        layoutReposError.setGone()
        textReposError.text = ""
    }

    private fun showContent(pullRequestList: List<PullRequest>) {
        hideLoadingView()
        hideErrorView()

        pullRequestList.map {
            Log.d(PullRequestListActivity::class.java.simpleName, it.toString())
        }

        recyclerViewRepos.setVisible()
    }

    private fun hideContent() {
        recyclerViewRepos.setGone()
    }
    //endregion
}
