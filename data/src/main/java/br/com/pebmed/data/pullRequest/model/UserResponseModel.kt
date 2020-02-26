package br.com.pebmed.data.pullRequest.model

import br.com.pebmed.domain.entities.UserModel
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
) {
    fun mapTo() = UserModel (
        login = this.login,
        avatarUrl = this.avatarUrl
    )
}