package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.entities.UserModel
import br.com.pebmed.domain.extensions.fromJsonGeneric
import com.google.gson.Gson
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

object UsefulObjects {
    fun loadUser() = UserModel(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequest(user: UserModel) = PullRequestModel(
        number = 1,
        htmlUrl = "http://the.url",
        title = "Title",
        user = this.loadUser(),
        body = "Body",
        createdAt = Date(),
        comments = 1,
        commits = 1,
        additions = 1,
        deletions = 1,
        changedFiles = 1
    )

    fun loadListPullRequestsUseCaseParams() = ListPullRequestsUseCase.Params(
        owner = "OwnerModel",
        repoName = "RepoName"
    )

    fun loadRepos(): List<RepoModel> {
        return Gson().fromJsonGeneric(readJsonFile("repos.json"))
    }

    @Throws(IOException::class)
    fun readJsonFile(filename: String): String {
        val loader = ClassLoader.getSystemClassLoader()

        val resourceLoaded = loader.getResource(filename)

        resourceLoaded?.let {
            return Files.lines(Paths.get(it.toURI()))
                .parallel()
                .collect(Collectors.joining())
        } ?: throw Throwable("Error while loading resource: $filename")
    }
}
