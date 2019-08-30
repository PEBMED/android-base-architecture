package br.com.pebmed.data.local.mapper

import com.example.basearch.data.BaseMapper
import br.com.pebmed.data.local.model.RepoCache
import br.com.pebmed.domain.entities.Repo

object RepoCacheMapper : BaseMapper<RepoCache, Repo> {
    override fun map(from: RepoCache) = Repo(
        id = from.id,
        description = from.description,
        disabled = from.disabled,
        fork = from.fork,
        forks = from.forks,
        forksCount = from.forksCount,
        forksUrl = from.forksUrl,
        fullName = from.fullName,
        language = from.language,
        name = from.name,
        openIssues = from.openIssues,
        openIssuesCount = from.openIssuesCount,
        owner = OwnerCacheMapper.map(from.owner),
        score = from.score,
        updatedAt = from.updatedAt,
        url = from.url,
        watchers = from.watchers,
        watchersCount = from.watchersCount,
        stargazersCount = from.stargazersCount
    )
}