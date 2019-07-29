package com.example.basearch.data.local.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Owner")
data class OwnerCache(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "events_url") val eventsUrl: String?,
    @ColumnInfo(name = "followers_url") val followersUrl: String?,
    @ColumnInfo(name = "following_url") val followingUrl: String?,
    @ColumnInfo(name = "gists_url") val gistsUrl: String?,
    @ColumnInfo(name = "gravatar_id") val gravatarId: String?,
    @ColumnInfo(name = "html_url") val htmlUrl: String?,
    @ColumnInfo(name = "node_id") val nodeId: String?,
    @ColumnInfo(name = "organizations_url") val organizationsUrl: String?,
    @ColumnInfo(name = "repos_url") val reposUrl: String?,
    @ColumnInfo(name = "site_admin") val siteAdmin: Boolean?,
    @ColumnInfo(name = "starred_url") val starredUrl: String?,
    @ColumnInfo(name = "subscriptions_url") val subscriptionsUrl: String?,
    val login: String?,
    val type: String?,
    val url: String?
)