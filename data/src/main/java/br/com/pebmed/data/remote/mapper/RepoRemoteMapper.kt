package br.com.pebmed.data.remote.mapper

import com.example.basearch.data.BaseMapper
import br.com.pebmed.data.remote.model.RepoPayload
import br.com.pebmed.domain.entities.Repo

object RepoRemoteMapper : BaseMapper<RepoPayload, Repo> {
    override fun map(from: RepoPayload) = Repo(
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
        owner = OwnerRemoteMapper.map(from.owner),
        score = from.score,
        updatedAt = from.updatedAt,
        url = from.url,
        watchers = from.watchers,
        watchersCount = from.watchersCount,
        stargazersCount = from.stargazersCount
    )
}