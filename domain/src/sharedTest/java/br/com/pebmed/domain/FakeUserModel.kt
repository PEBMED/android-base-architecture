package br.com.pebmed.domain

import br.com.pebmed.domain.entities.UserModel

class FakeUserModel {
    
    companion object {
        fun loadUser() = UserModel(
            login = "luis.fernandez",
            avatarUrl = "http://avatar.url"
        )
    }
}