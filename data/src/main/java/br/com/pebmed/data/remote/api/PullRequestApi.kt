package br.com.pebmed.data.remote.api

import br.com.pebmed.data.remote.model.response.PullRequestResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PullRequestApi {

    @GET("/repos/{owner}/{repoName}/pulls?state=all")
    suspend fun listPullRequestsAsync(
        @Path("owner") owner: String,
        @Path("repoName") repoName: String
    ): Response<List<PullRequestResponse>>

    @GET("/repos/{owner}/{repoName}/pulls/{pullRequestNumber}")
    suspend fun getPullRequestAsync(
        @Path("owner") owner: String,
        @Path("repoName") repoName: String,
        @Path("pullRequestNumber") pullRequestNumber: Long
    ): Response<PullRequestResponse>
}