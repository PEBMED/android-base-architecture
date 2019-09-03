package br.com.pebmed.domain.entities

import java.util.*

data class PullRequest(
    val htmlUrl: String,
    val title: String,
    val user: User,
    val body: String,
    val createdAt: Date
)