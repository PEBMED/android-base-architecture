package br.com.pebmed.data.repo.remote.model

import com.google.gson.annotations.SerializedName

data class GetReposResponse(
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val repos: List<RepoResponse>,
    @SerializedName("total_count") val totalCount: Int
)