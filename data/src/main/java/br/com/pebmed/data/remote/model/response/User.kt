package br.com.pebmed.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
)