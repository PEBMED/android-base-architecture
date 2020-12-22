package br.com.pebmed.domain

import br.com.pebmed.domain.entities.UserModel

class MockUserModel {
    
    companion object {
        fun mock() = UserModel(
            login = "luis.fernandez",
            avatarUrl = "http://avatar.url"
        )
    }
}