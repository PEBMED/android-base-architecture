package com.example.basearch.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basearch.R

class PullRequestActivity : AppCompatActivity() {

    private lateinit var owner: String
    private lateinit var repoName: String
    private var pullRequestId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_request)

        this.initObservers()

        owner = intent.extras?.getString("owner").toString()
        repoName = intent.extras?.getString("repoName").toString()
        pullRequestId = intent.extras?.getLong("pullRequestId")!!
    }

    private fun initObservers() {

    }
}
