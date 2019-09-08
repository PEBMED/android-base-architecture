package com.example.basearch.presentation.ui

sealed class ViewStateResource<SUCCESS, ERROR> {
    class Success<SUCCESS, ERROR>(
        val data: SUCCESS
    ) : ViewStateResource<SUCCESS, ERROR>()

    class Error<SUCCESS, ERROR>(
        val error: ERROR? = null
    ) : ViewStateResource<SUCCESS, ERROR>()

    class Empty<SUCCESS, ERROR>: ViewStateResource<SUCCESS, ERROR>()

    class Loading<SUCCESS, ERROR>: ViewStateResource<SUCCESS, ERROR>()
}