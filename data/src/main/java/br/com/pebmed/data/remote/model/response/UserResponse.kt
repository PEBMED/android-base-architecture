package br.com.pebmed.data.remote.model.response

import br.com.pebmed.domain.entities.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
) {
    fun mapTo() = User (
        login = this.login,
        avatarUrl = this.avatarUrl
    )
}