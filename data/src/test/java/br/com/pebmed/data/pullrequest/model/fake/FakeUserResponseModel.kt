package br.com.pebmed.data.pullrequest.model.fake

import br.com.pebmed.data.pullRequest.model.UserResponseModel

class FakeUserResponseModel {
    companion object {
        fun loadUserResponse() = UserResponseModel(
            login = "luis.fernandez",
            avatarUrl = "http://avatar.url"
        )
    }
}