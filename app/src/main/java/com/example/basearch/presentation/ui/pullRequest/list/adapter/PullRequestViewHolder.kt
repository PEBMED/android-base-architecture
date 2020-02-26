package com.example.basearch.presentation.ui.pullRequest.list.adapter

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.PullRequestModel
import br.com.pebmed.domain.extensions.toCacheFormat
import com.bumptech.glide.Glide
import com.example.basearch.R
import com.example.basearch.presentation.extensions.isValidForGlide
import kotlinx.android.synthetic.main.item_pull_request_list.view.*

class PullRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(
        activity: Activity,
        pullRequest: PullRequestModel,
        listener: PullRequestListAdapterListener
    ) {
        itemView.textAuthorName.text = pullRequest.user.login
        itemView.textTitle.text = pullRequest.title
        itemView.textDescription.text = pullRequest.body
        itemView.textDate.text = pullRequest.createdAt.toCacheFormat()

        if (activity.isValidForGlide()) {
            Glide.with(activity)
                .load(pullRequest.user.avatarUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(itemView.imagePullRequestAuthor)
        }

        itemView.setOnClickListener {
            listener.onItemClick(pullRequest.number)
        }
    }
}