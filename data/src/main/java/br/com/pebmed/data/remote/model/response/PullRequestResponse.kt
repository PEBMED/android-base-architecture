package br.com.pebmed.data.remote.model.response

import br.com.pebmed.domain.entities.PullRequest
import com.google.gson.annotations.SerializedName
import java.util.*

data class PullRequestResponse(
    @SerializedName("number") val number: Long,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("body") val body: String,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("comments") val comments: Int,
    @SerializedName("commits") val commits: Int,
    @SerializedName("additions") val additions: Int,
    @SerializedName("deletions") val deletions: Int,
    @SerializedName("changed_files") val changedFiles: Int
) {
    fun mapTo() = PullRequest (
        number = this.number,
        htmlUrl = this.htmlUrl,
        title = this.title,
        user = this.user.mapTo(),
        body = this.body,
        createdAt = this.createdAt,
        comments = this.comments,
        commits = this.commits,
        additions = this.additions,
        deletions = this.deletions,
        changedFiles = this.changedFiles
    )
}