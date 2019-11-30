package com.example.basearch.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.PullRequest
import com.bumptech.glide.RequestManager
import com.example.basearch.R
import kotlinx.android.synthetic.main.item_pull_request_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class PullRequestListAdapter(
    private val pullRequestList: MutableList<PullRequest>,
    private val requestManager: RequestManager,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PullRequestListAdapter.PullRequestViewHolder>() {

    class PullRequestViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindView(pullRequest: PullRequest, requestManager: RequestManager) {
            itemView.textAuthorName.text = pullRequest.user.login
            itemView.textTitle.text = pullRequest.title
            itemView.textDescription.text = pullRequest.body
            itemView.textDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(pullRequest.createdAt)

            requestManager.load(pullRequest.user.avatarUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(itemView.imagePullRequestAuthor)

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(
                    pullRequestNumber = pullRequest.number
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pull_request_list, parent, false)
        return PullRequestViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return pullRequestList.count()
    }

    override fun onBindViewHolder(holder: PullRequestViewHolder, position: Int) {
        val item = pullRequestList[position]

        holder.bindView(item, requestManager)
    }

    fun addItems(pullRequestList: List<PullRequest>) {
        val oldSize = this.pullRequestList.size
        this.pullRequestList.addAll(pullRequestList)
        notifyItemRangeInserted(oldSize, pullRequestList.size)
    }

    fun addItem(pullRequest: PullRequest) {
        pullRequestList.add(pullRequest)
        notifyItemInserted(pullRequestList.size - 1)
    }

    fun isEmpty() = pullRequestList.isEmpty()

    interface OnItemClickListener {
        fun onItemClick(pullRequestNumber: Long)
    }
}