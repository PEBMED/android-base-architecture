package br.com.pebmed.data.repo.remote

import br.com.pebmed.data.repo.remote.model.GetReposResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApi {
    @GET("/search/repositories?sort=stars")
    suspend fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") language: String
    ): Response<GetReposResponse>
}