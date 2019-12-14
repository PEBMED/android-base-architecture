package com.example.basearch.presentation.extensions

import androidx.lifecycle.ViewModel
import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper
import com.example.basearch.presentation.ui.base.ViewState

/**
 * Retorna um ViewState de acordo com o resultado do ResultWrapper passado
 *
 * @param SUCCESS modelo de sucesso
 */
fun <SUCCESS, ERROR> ViewModel.loadViewState(resultWrapper: ResultWrapper<SUCCESS, ERROR>): ViewState<SUCCESS, ERROR> {
    return if (resultWrapper.success != null) {
        handleSuccessfullViewState(resultWrapper.success!!)
    } else {
        ViewState.Error(error = resultWrapper.error)
    }
}

/**
 * Retorna estados de sucesso, incluindo o caso de modelo de sucesso ser uma lista
 */
private fun <SUCCESS, ERROR> handleSuccessfullViewState(data: SUCCESS): ViewState<SUCCESS, ERROR> {
    return if (data is List<*> && data.isNullOrEmpty()) {
        ViewState.Empty()
    } else {
        ViewState.Success(data)
    }
}