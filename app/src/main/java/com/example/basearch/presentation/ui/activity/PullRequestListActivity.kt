package com.example.basearch.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pebmed.domain.entities.PullRequest
import com.bumptech.glide.Glide
import com.example.basearch.R
import com.example.basearch.presentation.extensions.setGone
import com.example.basearch.presentation.extensions.setVisible
import com.example.basearch.presentation.ui.viewmodel.PullRequestListViewModel
import com.example.basearch.presentation.ui.base.ViewState
import com.example.basearch.presentation.ui.adapter.PullRequestListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PullRequestListActivity : AppCompatActivity(), PullRequestListAdapter.OnItemClickListener {

    private val viewModel by viewModel<PullRequestListViewModel>()
    private lateinit var adapter: PullRequestListAdapter

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
                is ViewState.Loading -> {
                    showLoadingView()
                }

                is ViewState.Success -> {
                    showContent(it.data)
                }

                is ViewState.Empty -> {
                    val message = getString(R.string.empty_list)
                }

                is ViewState.Error -> {
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
        adapter = PullRequestListAdapter(
            mutableListOf(),
            Glide.with(this),
            this
        )

        recyclerViewRepos.layoutManager = LinearLayoutManager(this)
        recyclerViewRepos.adapter = adapter
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

        adapter.addItems(pullRequestList)

        recyclerViewRepos.setVisible()
    }

    private fun hideContent() {
        recyclerViewRepos.setGone()
    }
    //endregion

    override fun onItemClick(pullRequestNumber: Long) {
        val intent = Intent(this, PullRequestActivity::class.java)

        intent.putExtra("owner", owner)
        intent.putExtra("repoName", repoName)
        intent.putExtra("pullRequestNumber", pullRequestNumber)

        startActivity(intent)
    }
}
