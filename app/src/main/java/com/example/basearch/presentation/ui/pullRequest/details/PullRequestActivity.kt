package com.example.basearch.presentation.ui.pullRequest.details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.extensions.toCacheFormat
import com.bumptech.glide.Glide
import com.example.basearch.R
import com.example.basearch.presentation.extensions.setGone
import com.example.basearch.presentation.extensions.setVisible
import com.example.basearch.presentation.ui.base.Navigator
import com.example.basearch.presentation.ui.base.ViewState
import kotlinx.android.synthetic.main.activity_pull_request.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PullRequestActivity : AppCompatActivity() {
    companion object {
        fun open(from: Context, owner: String, repoName: String, pullRequestNumber: Long) {
            val bundle = Bundle()

            bundle.putString("ownerModel", owner)
            bundle.putString("repoName", repoName)
            bundle.putLong("pullRequestNumber", pullRequestNumber)

            Navigator.goToActivity(from, PullRequestActivity::class.java, bundle)
        }
    }

    private val viewModel by viewModel<PullRequestViewModel>()

    private lateinit var owner: String
    private lateinit var repoName: String
    private var pullRequestNumber: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_request)

        owner = intent.extras?.getString("ownerModel") ?: ""
        repoName = intent.extras?.getString("repoName") ?: ""
        pullRequestNumber = intent.extras?.getLong("pullRequestNumber") ?: -1

        this.initToolbar()
        this.initObservers()

        viewModel.getPullRequest(owner, repoName, pullRequestNumber)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Pull Request Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initObservers() {
        viewModel.pullRequestState.observe(this, Observer {
            when (it) {
                is ViewState.Loading -> {
                    showLoadingView()
                }

                is ViewState.Success -> {
                    showContent(it.data)
                }

                is ViewState.Error -> {
                    showErrorView(it.error.toString())
                }
            }
        })
    }

    //region ViewStates
    private fun showLoadingView() {
        hideErrorView()
        hideContent()

        layoutLoading.setVisible()
    }

    private fun hideLoadingView() {
        layoutLoading.setGone()
    }

    private fun showErrorView(message: String) {
        hideLoadingView()
        hideContent()

        textPullRequestError.text = message
        layoutError.setVisible()
    }

    private fun hideErrorView() {
        layoutError.setGone()
        textPullRequestError.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun showContent(pullRequest: PullRequestModel) {
        hideLoadingView()
        hideErrorView()

        textAuthorName.text = pullRequest.user.login
        textTitle.text = pullRequest.title
        textDescription.text = pullRequest.body
        textComments.text = "Comments: ${pullRequest.comments}"
        textCommits.text = "Commits: ${pullRequest.commits}"
        textAdditions.text = "Additions: ${pullRequest.additions}"
        textDeletions.text = "Deletions: ${pullRequest.deletions}"
        textChangedFiles.text = "Changed Files: ${pullRequest.changedFiles}"

        textDate.text = pullRequest.createdAt.toCacheFormat()

        Glide.with(this).load(pullRequest.user.avatarUrl)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(imagePullRequestAuthor)

        layoutContent.setVisible()
    }

    private fun hideContent() {
        layoutContent.setGone()
    }
    //endregion
}
