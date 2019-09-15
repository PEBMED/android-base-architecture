package br.com.pebmed.data.local.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.pebmed.domain.entities.Owner

@Entity(tableName = "Owner")
data class OwnerCache(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "gravatar_id") val gravatarId: String?,
    @ColumnInfo(name = "html_url") val htmlUrl: String?,
    @ColumnInfo(name = "node_id") val nodeId: String?,
    @ColumnInfo(name = "site_admin") val siteAdmin: Boolean?,
    @ColumnInfo(name = "starred_url") val starredUrl: String?,
    @ColumnInfo(name = "subscriptions_url") val subscriptionsUrl: String?,
    val login: String?,
    val type: String?,
    val url: String?
) {

    fun mapTo() = Owner(
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
