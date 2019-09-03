package br.com.pebmed.data.remote.mapper

import br.com.pebmed.data.remote.model.response.UserResponse
import br.com.pebmed.domain.entities.User
import com.example.basearch.data.BaseMapper

object UserRemoteMapper : BaseMapper<UserResponse, User> {

    override fun map(from: UserResponse) = User (
        login = from.login,
        avatarUrl = from.avatarUrl
    )
}