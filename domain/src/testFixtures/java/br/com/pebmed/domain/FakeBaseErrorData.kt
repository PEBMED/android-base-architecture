package br.com.pebmed.domain

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.PullRequestModel

class FakeBaseErrorData {

    companion object {
        fun mockStringError() = BaseErrorData (
            errorBody = "# TEST MESSAGE"
        )

        fun mockStatusError() = BaseErrorData (
            errorBody = BaseErrorStatus.DEFAULT_ERROR
        )

        fun mockBaseErrorStatus_Default() = BaseErrorStatus.DEFAULT_ERROR

//        fun <ERROR_BODY> fakeError(
//            error: ERROR_BODY? = null,
//            errorMessage: String? = null
//        ): BaseErrorData<ERROR_BODY> {
//            return BaseErrorData(
//                errorBody = error,
//                errorMessage = errorMessage
//            )
//        }
    }

}