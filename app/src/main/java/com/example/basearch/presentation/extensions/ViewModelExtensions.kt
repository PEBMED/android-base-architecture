package com.example.basearch.presentation.extensions

import androidx.lifecycle.ViewModel
import br.com.pebmed.domain.base.ResultWrapper
import com.example.basearch.presentation.ui.ViewStateResource

fun <SUCCESS, ERROR> ViewModel.loadViewStateResourceList(resultWrapper: ResultWrapper<SUCCESS, ERROR>): ViewStateResource<SUCCESS, ERROR> {

    return when (resultWrapper) {
        is ResultWrapper.Success -> {
            val data = resultWrapper.data

            if (data != null) {
                val list = data as List<*>

                return if (list.isNullOrEmpty()) {
                    ViewStateResource.Empty()
                } else {
                    ViewStateResource.Success(data)
                }
            } else {
                ViewStateResource.Empty()
            }
        }

        is ResultWrapper.Error -> {
            ViewStateResource.Error(error = resultWrapper.error)
        }
    }
}

fun <SUCCESS, ERROR> ViewModel.loadViewStateResource(resultWrapper: ResultWrapper<SUCCESS, ERROR>): ViewStateResource<SUCCESS, ERROR> {

    return when (resultWrapper) {
        is ResultWrapper.Success -> {
            val data = resultWrapper.data

            if (data != null) {
                ViewStateResource.Success(data)
            } else {
                ViewStateResource.Empty()
            }
        }

        is ResultWrapper.Error -> {
            ViewStateResource.Error(error = resultWrapper.error)
        }
    }
}