package br.com.pebmed.data.remote.mapper

import com.example.basearch.data.BaseMapper
import br.com.pebmed.data.remote.model.OwnerPayload
import br.com.pebmed.domain.entities.Owner

object OwnerRemoteMapper : BaseMapper<OwnerPayload, Owner> {
    override fun map(from: OwnerPayload) = Owner(
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