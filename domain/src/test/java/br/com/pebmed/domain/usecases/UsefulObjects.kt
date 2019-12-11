package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.entities.PullRequest
import br.com.pebmed.domain.entities.Repo
import br.com.pebmed.domain.entities.User
import br.com.pebmed.domain.extensions.fromJsonGeneric
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

object UsefulObjects {
    fun loadUser() = User(
        login = "luis.fernandez",
        avatarUrl = "http://avatar.url"
    )

    fun loadPullRequest(user: User) = PullRequest(
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
        owner = "Owner",
        repoName = "RepoName"
    )

    fun loadRepos(): List<Repo> {
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
