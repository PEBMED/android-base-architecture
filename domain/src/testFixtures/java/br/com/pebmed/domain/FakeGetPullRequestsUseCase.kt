package br.com.pebmed.domain

import br.com.pebmed.domain.usecases.GetPullRequestsUseCase

class FakeGetPullRequestsUseCase {

    class Params {

        companion object {
            fun mock() = GetPullRequestsUseCase.Params(
                owner = "OwnerModel",
                repoName = "RepoName"
            )
        }
    }
}