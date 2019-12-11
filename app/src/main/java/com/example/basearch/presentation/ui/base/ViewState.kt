package com.example.basearch.presentation.ui.base

/**
 * Representa os possíveis estados de uma tela
 */
sealed class ViewState<SUCCESS, ERROR> {
    class Success<SUCCESS, ERROR>(
        val data: SUCCESS
    ) : ViewState<SUCCESS, ERROR>()

    class Error<SUCCESS, ERROR>(
        val error: ERROR? = null
    ) : ViewState<SUCCESS, ERROR>()

    class Empty<SUCCESS, ERROR>: ViewState<SUCCESS, ERROR>()

    class Loading<SUCCESS, ERROR>: ViewState<SUCCESS, ERROR>()
}