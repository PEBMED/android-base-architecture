package br.com.pebmed.domain.entities

import com.google.gson.annotations.SerializedName

data class GitHubErrorData(
    @SerializedName("message") val message: String,
    @SerializedName("documentation_url") val documentationUrl: String
)