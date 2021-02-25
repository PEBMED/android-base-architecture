package com.pebmed.basearch.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pebmed.domain.entities.RepoModel
import com.pebmed.basearch.R
import com.pebmed.basearch.presentation.extensions.setGone
import com.pebmed.basearch.presentation.extensions.setVisible
import com.pebmed.basearch.presentation.extensions.showToast
import com.pebmed.basearch.presentation.ui.base.EndlessRecyclerView
import com.pebmed.basearch.presentation.ui.base.Navigator
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.basearch.presentation.ui.billing.BillingActivity
import com.pebmed.basearch.presentation.ui.main.adapter.ReposAdapter
import com.pebmed.basearch.presentation.ui.main.adapter.ReposAdapterListener
import com.pebmed.basearch.presentation.ui.pullRequest.list.PullRequestListActivity
import com.pebmed.platform.network.NetworkConnectivityManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), EndlessRecyclerView.Callback {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var reposAdapter: ReposAdapter
    private val networkConnectivityManager by inject<NetworkConnectivityManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initReposRecyclerView()
        initObservers()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_billing -> {
                goToBillingActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initObservers() {
        networkConnectivityManager.isConnectedEvent.observe(this, Observer {
            Log.i("MainActivity","Network status changed: $it")
        })

        viewModel.reposState.observe(this, Observer {
            when (it) {
                is ViewState.Loading -> {
                    if(reposAdapter.isEmpty()) {
                        showLoadingView()
                    }
                }

                is ViewState.Success -> {
                    recyclerViewRepos.apply {
                        stopPaging()
                        nextPage(2)
                        hasNextPage(true)
                    }
                    showReposList(it.data)
                }

                is ViewState.Empty -> {
                    val message = getString(R.string.empty_list)

                    when {
                        reposAdapter.isEmpty() -> showErrorView(message)
                        else -> showToast(message)
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
        reposAdapter = ReposAdapter(
            this,
            mutableListOf(),
            object : ReposAdapterListener {
                override fun onItemClick(repo: RepoModel) {
                    PullRequestListActivity.open(
                        this@MainActivity,
                        repo.ownerModel.login!!,
                        repo.name!!
                    )
                }

            }
        )

        recyclerViewRepos.layoutManager = LinearLayoutManager(this)
        recyclerViewRepos.adapter = reposAdapter
        recyclerViewRepos.callback(this)
    }

    override fun loadMore(nextPage: Int) {
        Toast.makeText(baseContext, "LoadMore", Toast.LENGTH_SHORT).show()
        viewModel.loadRepos(nextPage)
    }

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

    private fun showReposList(repos: List<RepoModel>) {
        hideLoadingView()
        hideErrorView()

        reposAdapter.addItems(repos)

        recyclerViewRepos.setVisible()
    }

    private fun hideReposList() {
        recyclerViewRepos.setGone()
    }

    private fun goToBillingActivity() {
        if (networkConnectivityManager.isConnectedEvent.value == true) {
            Navigator.goToActivity(this@MainActivity, BillingActivity::class.java)
        } else {
            showToast("Você precisa estar conectado a internet para acessar essa funcionalidade.")
        }
    }
    //endregion
}
