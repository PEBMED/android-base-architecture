package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.entities.UserModel
import br.com.pebmed.domain.extensions.fromJsonGeneric
import br.com.pebmed.domain.usecases.GetPullRequestsUseCase
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
