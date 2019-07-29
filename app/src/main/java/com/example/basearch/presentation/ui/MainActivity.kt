package com.example.basearch.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.basearch.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.reposState.observe(this, Observer {
            when (it) {
                is ViewStateResource.Loading -> {
                    val msg = getString(R.string.default_loading_message)
                    textView.text = msg
                }

                is ViewStateResource.Success -> {
                    val msg = it.data.size
                    textView.text = "List size: $msg"
                }

                is ViewStateResource.Empty -> {
                    textView.text = "Empty result"
                }

                is ViewStateResource.Error -> {
                    val msg = it.message ?: "Default Error"
                    textView.text = msg
                }
            }
        })

        button.setOnClickListener {
            viewModel.test()
        }
    }
}
