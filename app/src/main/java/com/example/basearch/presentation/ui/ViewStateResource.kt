package com.example.basearch.presentation.ui

sealed class ViewStateResource<SUCCESS> {
    class Success<SUCCESS>(
        val data: SUCCESS
    ) : ViewStateResource<SUCCESS>()

    class Error<SUCCESS>(
        val message: String?
    ) : ViewStateResource<SUCCESS>()

    class Empty<SUCCESS> : ViewStateResource<SUCCESS>()

    class Loading<SUCCESS> : ViewStateResource<SUCCESS>()
}