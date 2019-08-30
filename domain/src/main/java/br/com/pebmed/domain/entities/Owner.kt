package br.com.pebmed.domain.entities

data class Owner(
    val avatarUrl: String?,
    val gravatarId: String?,
    val htmlUrl: String?,
    val id: Int,
    val login: String?,
    val nodeId: String?,
    val siteAdmin: Boolean?,
    val starredUrl: String?,
    val subscriptionsUrl: String?,
    val type: String?,
    val url: String?
)