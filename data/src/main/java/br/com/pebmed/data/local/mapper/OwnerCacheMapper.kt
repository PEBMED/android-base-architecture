package br.com.pebmed.data.local.mapper

import com.example.basearch.data.BaseMapper
import br.com.pebmed.data.local.model.OwnerCache
import br.com.pebmed.domain.entities.Owner

object OwnerCacheMapper : BaseMapper<OwnerCache, Owner> {
    override fun map(from: OwnerCache) = Owner(
        id = from.id,
        avatarUrl = from.avatarUrl,
        gravatarId = from.gravatarId,
        htmlUrl = from.htmlUrl,
        login = from.login,
        nodeId = from.nodeId,
        siteAdmin = from.siteAdmin,
        starredUrl = from.starredUrl,
        subscriptionsUrl = from.subscriptionsUrl,
        type = from.type,
        url = from.url
    )
}