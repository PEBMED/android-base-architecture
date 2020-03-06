package br.com.pebmed.domain

import br.com.pebmed.domain.usecases.GetPullRequestsUseCase

class FakeGetPullRequestsUseCase {

    class Params {

        companion object {
            fun loadListPullRequestsUseCaseParams() = GetPullRequestsUseCase.Params(
                owner = "OwnerModel",
                repoName = "RepoName"
            )
        }
    }
}