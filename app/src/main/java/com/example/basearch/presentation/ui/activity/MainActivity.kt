package com.example.basearch.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pebmed.domain.entities.Repo
import com.bumptech.glide.Glide
import com.example.basearch.R
import com.example.basearch.presentation.extensions.setGone
import com.example.basearch.presentation.extensions.setVisible
import com.example.basearch.presentation.extensions.showToast
import com.example.basearch.presentation.ui.adapter.ReposAdapter
import com.example.basearch.presentation.ui.base.ViewState
import com.example.basearch.presentation.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initReposRecyclerView()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.reposState.observe(this, Observer {
            when (it) {
                is ViewState.Loading -> {
                    showLoadingView()
                }

                is ViewState.Success -> {
                    showReposList(it.data!!)
                }

                is ViewState.Empty -> {
                    val message = getString(R.string.empty_list)

                    if (reposAdapter.isEmpty()) {
                        showErrorView(message)
                    } else {
                        showToast(message)
                    }
                }

                is ViewState.Error -> {
                    val baseErrorData = it.error
                    val message = baseErrorData?.errorMessage ?: getString(R.string.error_message)

                    if (reposAdapter.isEmpty()) {
                        showErrorView(message)
                    } else {
                        showToast(message)
                    }
                }
            }
        })
    }

    private fun initListeners() {
        buttonReposTryAgain.setOnClickListener {
            viewModel.loadRepos()
        }
    }

    private fun initReposRecyclerView() {
        reposAdapter =
            ReposAdapter(
                mutableListOf(),
                Glide.with(this)
            )

        recyclerViewRepos.layoutManager = LinearLayoutManager(this)
        recyclerViewRepos.adapter = reposAdapter
    }

    //region ViewStates
    private fun showLoadingView() {
        hideErrorView()
        hideReposList()

        layoutReposLoading.setVisible()
    }

    private fun hideLoadingView() {
        layoutReposLoading.setGone()
    }

    private fun showErrorView(message: String) {
        hideLoadingView()
        hideReposList()

        textReposError.text = message
        layoutReposError.setVisible()
    }

    private fun hideErrorView() {
        layoutReposError.setGone()
        textReposError.text = ""
    }

    private fun showReposList(repos: List<Repo>) {
        hideLoadingView()
        hideErrorView()

        reposAdapter.addItems(repos)

        recyclerViewRepos.setVisible()
    }

    private fun hideReposList() {
        recyclerViewRepos.setGone()
    }
    //endregion
}
