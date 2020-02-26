package com.example.basearch.presentation.ui.main.adapter

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.RepoModel
import com.bumptech.glide.Glide
import com.example.basearch.R
import com.example.basearch.presentation.extensions.isValidForGlide
import kotlinx.android.synthetic.main.item_repo_list.view.*

class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(activity: Activity, repo: RepoModel, listener: ReposAdapterListener) {
        itemView.textRepoName.text = repo.name
        itemView.textRepoAuthor.text = repo.ownerModel.login
        itemView.textRepoDescription.text = repo.description
        itemView.textStarsCount.text = repo.stargazersCount.toString()
        itemView.textForksCount.text = repo.forksCount.toString()
        itemView.textIssuesCount.text = repo.openIssuesCount.toString()

        if (activity.isValidForGlide()) {
            Glide.with(activity)
                .load(repo.ownerModel.avatarUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(itemView.imageRepoAuthor)
        }

        itemView.setOnClickListener {
            listener.onItemClick(repo)
        }
    }
}