package br.com.pebmed.data.remote.model.response

import br.com.pebmed.domain.entities.PullRequest
import com.google.gson.annotations.SerializedName
import java.util.*

data class PullRequestResponse(
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("body") val body: String,
    @SerializedName("created_at") val createdAt: Date
) {
    fun mapTo() = PullRequest (
        htmlUrl = this.htmlUrl,
        title = this.title,
        user = this.user.mapTo(),
        body = this.body,
        createdAt = this.createdAt
    )
}