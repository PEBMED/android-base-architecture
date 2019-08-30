package br.com.pebmed.domain.entities

data class Repo(
    val description: String?,
    val disabled: Boolean?,
    val fork: Boolean?,
    val forks: Int?,
    val forksCount: Int?,
    val forksUrl: String?,
    val fullName: String?,
    val id: Int,
    val language: String?,
    val name: String?,
    val openIssues: Int?,
    val openIssuesCount: Int?,
    val owner: Owner,
    val score: Double?,
    val updatedAt: String?,
    val url: String?,
    val watchers: Int?,
    val watchersCount: Int?,
    val stargazersCount: Int?
)