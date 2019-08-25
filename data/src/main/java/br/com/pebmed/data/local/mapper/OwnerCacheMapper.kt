package com.example.basearch.data.local.mapper

import com.example.basearch.data.BaseMapper
import com.example.basearch.data.local.model.OwnerCache
import com.example.basearch.domain.entities.Owner

object OwnerCacheMapper : BaseMapper<OwnerCache, Owner> {
    override fun map(from: OwnerCache) = Owner(
        id = from.id,
        avatarUrl = from.avatarUrl,
        eventsUrl = from.eventsUrl,
        followersUrl = from.followersUrl,
        followingUrl = from.followingUrl,
        gistsUrl = from.gistsUrl,
        gravatarId = from.gravatarId,
        htmlUrl = from.htmlUrl,
        login = from.login,
        nodeId = from.nodeId,
        organizationsUrl = from.organizationsUrl,
        reposUrl = from.reposUrl,
        siteAdmin = from.siteAdmin,
        starredUrl = from.starredUrl,
        subscriptionsUrl = from.subscriptionsUrl,
        type = from.type,
        url = from.url
    )
}