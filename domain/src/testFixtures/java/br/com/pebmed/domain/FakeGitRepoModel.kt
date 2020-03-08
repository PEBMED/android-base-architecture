package br.com.pebmed.domain

import br.com.pebmed.domain.entities.RepoModel

class FakeGitRepoModel {
    
    companion object {
        fun mock(itemsOnList: Int): List<RepoModel> {
            val list = mutableListOf<RepoModel>()

            for (index in 0 .. itemsOnList) {
                list.add(
                    mock()
                )
            }

            return list
        }

        fun mock() = RepoModel(
            id = 1,
            ownerModel = FakeOwnerModel.mock()
        )
    }
}