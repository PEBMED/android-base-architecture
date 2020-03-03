package com.example.basearch.presentation.ui.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.RepoModel
import com.example.basearch.R

class ReposAdapter(
    private val activity: Activity,
    private val reposList: MutableList<RepoModel>,
    private val listener: ReposAdapterListener
) : RecyclerView.Adapter<ReposViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo_list, parent, false)
        return ReposViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reposList.count()
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repo = reposList[position]

        holder.bindView(activity, repo, listener)
    }

    fun addItems(repos: List<RepoModel>) {
        val oldSize = reposList.size
        reposList.addAll(repos)
        notifyItemRangeInserted(oldSize, repos.size)
    }

    fun addItem(repo: RepoModel) {
        reposList.add(repo)
        notifyItemInserted(reposList.size - 1)
    }

    fun isEmpty() = reposList.isEmpty()

}