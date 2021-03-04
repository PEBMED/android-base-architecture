package br.com.pebmed.domain.entities

data class RepoListModel(
    val listOfRepoModel: List<RepoModel>,
    val nextPage: Boolean? = false,
    val hasNextPage: Int? = 0
)