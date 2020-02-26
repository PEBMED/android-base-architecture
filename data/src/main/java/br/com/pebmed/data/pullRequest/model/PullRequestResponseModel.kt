package br.com.pebmed.data.pullRequest.model

import br.com.pebmed.domain.entities.PullRequestModel
import com.google.gson.annotations.SerializedName
import java.util.*

data class PullRequestResponseModel(
    @SerializedName("number") val number: Long,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("user") val userModel: UserResponseModel,
    @SerializedName("body") val body: String,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("comments") val comments: Int,
    @SerializedName("commits") val commits: Int,
    @SerializedName("additions") val additions: Int,
    @SerializedName("deletions") val deletions: Int,
    @SerializedName("changed_files") val changedFiles: Int
) {
    fun mapTo() = PullRequestModel (
        number = this.number,
        htmlUrl = this.htmlUrl,
        title = this.title,
        user = this.userModel.mapTo(),
        body = this.body,
        createdAt = this.createdAt,
        comments = this.comments,
        commits = this.commits,
        additions = this.additions,
        deletions = this.deletions,
        changedFiles = this.changedFiles
    )
}