package br.com.pebmed.data.repo.remote.model

import br.com.pebmed.domain.entities.OwnerModel
import com.google.gson.annotations.SerializedName

data class OwnerResponse(
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("events_url") val eventsUrl: String?,
    @SerializedName("followers_url") val followersUrl: String?,
    @SerializedName("following_url") val followingUrl: String?,
    @SerializedName("gists_url") val gistsUrl: String?,
    @SerializedName("gravatar_id") val gravatarId: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String?,
    @SerializedName("node_id") val nodeId: String?,
    @SerializedName("organizations_url") val organizationsUrl: String?,
    @SerializedName("received_events_url") val receivedEventsUrl: String?,
    @SerializedName("repos_url") val reposUrl: String?,
    @SerializedName("site_admin") val siteAdmin: Boolean?,
    @SerializedName("starred_url") val starredUrl: String?,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("url") val url: String?
) {
    fun mapTo() = OwnerModel(
        id = this.id,
        avatarUrl = this.avatarUrl,
        gravatarId = this.gravatarId,
        htmlUrl = this.htmlUrl,
        login = this.login,
        nodeId = this.nodeId,
        siteAdmin = this.siteAdmin,
        starredUrl = this.starredUrl,
        subscriptionsUrl = this.subscriptionsUrl,
        type = this.type,
        url = this.url
    )
}
