package br.com.pebmed.data.remote.source

import br.com.pebmed.data.remote.api.PullRequestApi
import br.com.pebmed.data.remote.model.response.PullRequestResponse
import br.com.pebmed.data.repository.BaseDataSourceImpl
import br.com.pebmed.data.repository.ExecuteApiAsync
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.ResultWrapper
import retrofit2.Response

class PullRequestRemoteDataSourceImpl(
    private val pullRequestApi: PullRequestApi
) : PullRequestRemoteDataSource, BaseDataSourceImpl() {

    override suspend fun listPullRequests(
        owner: String,
        repoName: String
    ): ResultWrapper<List<PullRequestResponse>, BaseErrorData<Void>> {
        return safeApiCall(object : ExecuteApiAsync<List<PullRequestResponse>> {
            override suspend fun execute(): Response<List<PullRequestResponse>> {
                return pullRequestApi.listPullRequestsAsync(owner, repoName)
            }
        })
    }
}