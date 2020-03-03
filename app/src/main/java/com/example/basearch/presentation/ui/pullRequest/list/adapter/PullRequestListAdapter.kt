package com.example.basearch.presentation.ui.pullRequest.list.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.PullRequestModel
import com.example.basearch.R

class PullRequestListAdapter(
    private val activity: Activity,
    private val pullRequestList: MutableList<PullRequestModel>,
    private val listener: PullRequestListAdapterListener
) : RecyclerView.Adapter<PullRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pull_request_list, parent, false)
        return PullRequestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pullRequestList.count()
    }

    override fun onBindViewHolder(holder: PullRequestViewHolder, position: Int) {
        val item = pullRequestList[position]

        holder.bindView(activity, item, listener)
    }

    fun addItems(pullRequestList: List<PullRequestModel>) {
        val oldSize = this.pullRequestList.size
        this.pullRequestList.addAll(pullRequestList)
        notifyItemRangeInserted(oldSize, pullRequestList.size)
    }

    fun addItem(pullRequest: PullRequestModel) {
        pullRequestList.add(pullRequest)
        notifyItemInserted(pullRequestList.size - 1)
    }

    fun isEmpty() = pullRequestList.isEmpty()
}