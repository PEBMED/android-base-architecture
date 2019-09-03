package br.com.pebmed.data.remote.mapper

import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.domain.entities.PullRequest
import com.example.basearch.data.BaseMapper

object PullRequestRemoteMapper : BaseMapper<PullRequestResponse, PullRequest> {

    override fun map(from: PullRequestResponse) = PullRequest (
        htmlUrl = from.htmlUrl,
        title = from.title,
        user = UserRemoteMapper.map(from.user),
        body = from.body,
        createdAt = from.createdAt
    )
}