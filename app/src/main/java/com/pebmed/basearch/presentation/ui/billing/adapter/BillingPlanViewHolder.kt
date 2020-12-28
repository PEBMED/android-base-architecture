package com.pebmed.basearch.presentation.ui.billing.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.billing.PlanModel
import kotlinx.android.synthetic.main.item_billing_plan.view.*

class BillingPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(plan: PlanModel, listener: BillingPlanListAdapterListener) {
        itemView.textBillingItemTitle.text = plan.title
        itemView.textBillingItemPrice.text = plan.price
        itemView.textBillingItemStoreId.text = plan.storeId
        itemView.textBillingItemCustomMessage.text = plan.customMessage

        itemView.setOnClickListener { listener.onItemClick(plan) }
    }
}