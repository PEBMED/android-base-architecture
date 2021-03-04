package br.com.pebmed.domain.entities

data class RepoListModel(
    val listOfRepoModel: List<RepoModel>,
    val hasNextPage: Boolean? = false,
    val nextPage: Int? = 0
)