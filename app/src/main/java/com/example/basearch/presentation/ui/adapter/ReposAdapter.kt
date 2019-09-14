package com.example.basearch.presentation.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.basearch.R
import br.com.pebmed.domain.entities.Repo
import com.example.basearch.presentation.ui.activity.PullRequestListActivity
import kotlinx.android.synthetic.main.item_repo_list.view.*

class ReposAdapter(private val reposList: MutableList<Repo>, private val requestManager: RequestManager) : RecyclerView.Adapter<ReposAdapter.ReposViewHolder>() {

    class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(repo: Repo, requestManager: RequestManager) {
            itemView.textRepoName.text = repo.name
            itemView.textRepoAuthor.text = repo.owner.login
            itemView.textRepoDescription.text = repo.description
            itemView.textStarsCount.text = repo.stargazersCount.toString()
            itemView.textForksCount.text = repo.forksCount.toString()
            itemView.textIssuesCount.text = repo.openIssuesCount.toString()

            requestManager.load(repo.owner.avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(itemView.imageRepoAuthor)

            itemView.setOnClickListener {
                val intent = Intent(
                    itemView.context,
                    PullRequestListActivity::class.java
                )

                intent.putExtra("owner", repo.owner.login)
                intent.putExtra("repoName", repo.name)

                itemView.context.startActivity(
                    intent
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo_list, parent, false)
        return ReposViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reposList.count()
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repo = reposList[position]

        holder.bindView(repo, requestManager)
    }

    fun addItems(repos: List<Repo>) {
        val oldSize = reposList.size
        reposList.addAll(repos)
        notifyItemRangeInserted(oldSize, repos.size)
    }

    fun addItem(repo: Repo) {
        reposList.add(repo)
        notifyItemInserted(reposList.size - 1)
    }

    fun isEmpty() = reposList.isEmpty()

}