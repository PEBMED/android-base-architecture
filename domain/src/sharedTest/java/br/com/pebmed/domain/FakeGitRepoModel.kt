package br.com.pebmed.domain

import br.com.pebmed.domain.entities.RepoModel
import br.com.pebmed.domain.extensions.fromJsonGeneric
import com.google.gson.Gson

class FakeGitRepoModel {
    
    companion object {
        fun loadRepos(): List<RepoModel> {
            return Gson().fromJsonGeneric(UsefulObjects.readJsonFile("repos.json"))
        }
    }
}