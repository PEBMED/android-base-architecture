package com.example.basearch.data.remote.mapper

import com.example.basearch.data.BaseMapper
import com.example.basearch.data.remote.model.OwnerPayload
import com.example.basearch.domain.entities.Owner

object OwnerRemoteMapper : BaseMapper<OwnerPayload, Owner> {
    override fun map(from: OwnerPayload) = Owner(
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