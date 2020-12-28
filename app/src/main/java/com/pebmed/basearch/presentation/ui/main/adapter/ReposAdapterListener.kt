package com.pebmed.basearch.presentation.ui.main.adapter

import br.com.pebmed.domain.entities.RepoModel

interface ReposAdapterListener {
    fun onItemClick(repo: RepoModel)
}