package br.com.pebmed.domain

import br.com.pebmed.domain.base.CompleteResultWrapper
import br.com.pebmed.domain.base.ResultWrapper

class FakeResultWrapper {

    companion object {
        fun <SUCCESS, ERROR> mockSuccess(success: SUCCESS): ResultWrapper<SUCCESS, ERROR> {
            return ResultWrapper(
                    success = success
            )
        }

        fun <SUCCESS, ERROR> mockError(error: ERROR): ResultWrapper<SUCCESS, ERROR> {
            return ResultWrapper(
                    error = error
            )
        }

        fun <SUCCESS, ERROR> mockCompleteSuccess(success: SUCCESS): ResultWrapper<SUCCESS, ERROR> {
            return CompleteResultWrapper(
                    success = success
            )
        }

        fun <SUCCESS, ERROR> mockCompleteError(error: ERROR): ResultWrapper<SUCCESS, ERROR> {
            return CompleteResultWrapper(
                    error = error
            )
        }
    }
}