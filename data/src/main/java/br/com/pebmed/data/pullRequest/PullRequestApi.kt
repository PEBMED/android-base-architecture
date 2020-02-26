package br.com.pebmed.data.pullRequest

import br.com.pebmed.data.pullRequest.model.PullRequestResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PullRequestApi {
    @GET("/repos/{ownerModel}/{repoName}/pulls?state=all")
    suspend fun getPullRequests(
        @Path("ownerModel") owner: String,
        @Path("repoName") repoName: String
    ): Response<List<PullRequestResponseModel>>

    @GET("/repos/{ownerModel}/{repoName}/pulls/{pullRequestNumber}")
    suspend fun getPullRequest(
        @Path("ownerModel") owner: String,
        @Path("repoName") repoName: String,
        @Path("pullRequestNumber") pullRequestNumber: Long
    ): Response<PullRequestResponseModel>
}