package br.com.pebmed.domain

import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.UserModel
import java.util.*

class MockPullRequestModel {

    companion object {
        val mockDate = Date()

        fun mockList(itemsOnList: Int = 1) : List<PullRequestModel> {
            val list = mutableListOf<PullRequestModel>()

            for (index in 1 .. itemsOnList) {
                list.add(
                    mock(
                        MockUserModel.mock()
                    )
                )
            }

            return list
        }

        fun mock(user: UserModel) = PullRequestModel(
            number = 1,
            htmlUrl = "http://the.url",
            title = "Title",
            user = user,
            body = "Body",
            createdAt = mockDate,
            comments = 1,
            commits = 1,
            additions = 1,
            deletions = 1,
            changedFiles = 1
        )
    }
}