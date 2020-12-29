package br.com.pebmed.domain

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel

object MockBaseErrorData {

    fun mockStringError() = BaseErrorData (errorBody = "# TEST MESSAGE")

    fun mockStatusError() = BaseErrorData (errorBody = BaseErrorStatus.DEFAULT_ERROR)

    fun mockBaseErrorStatusDefault() = BaseErrorStatus.DEFAULT_ERROR
}