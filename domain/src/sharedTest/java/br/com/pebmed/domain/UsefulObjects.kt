package br.com.pebmed.domain

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

object UsefulObjects {

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
