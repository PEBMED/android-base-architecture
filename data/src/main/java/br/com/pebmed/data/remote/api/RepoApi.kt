package br.com.pebmed.data.remote.api

import br.com.pebmed.data.remote.model.response.GetReposResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApi {
    @GET("/search/repositories?sort=stars&per_page=10")
    suspend fun getRepos(@Query("page") page: Int, @Query("q") language: String): Response<GetReposResponse>
}