package br.com.pebmed.domain

import br.com.pebmed.domain.usecases.GetPullRequestsUseCase

class MockGetPullRequestsUseCase {

    class Params {

        companion object {
            fun mock() = GetPullRequestsUseCase.Params(
                owner = "OwnerModel",
                repoName = "RepoName"
            )
        }
    }
}