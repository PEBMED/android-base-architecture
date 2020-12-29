package br.com.pebmed.data.pullrequest.model

import br.com.pebmed.data.pullRequest.model.UserResponseModel
import br.com.pebmed.domain.MockUserModel
import io.mockk.every
import io.mockk.mockk

object MockUserResponseModel {

    fun generic() : UserResponseModel {
        val mockedUserResponseModel = mockk<UserResponseModel>()
        every {
            mockedUserResponseModel.login
        } returns "luis.fernandez"

        every {
            mockedUserResponseModel.avatarUrl
        } returns "http://avatar.url"

        every {
            mockedUserResponseModel.mapTo()
        } returns MockUserModel.mock()

        return mockedUserResponseModel
    }
}