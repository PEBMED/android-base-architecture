package com.pebmed.basearch.presentation.ui.billing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pebmed.domain.entities.billing.PlanModel
import com.pebmed.basearch.R

class BillingPlanListAdapter(
    private val plans: List<PlanModel>,
    private val listener: BillingPlanListAdapterListener
) : RecyclerView.Adapter<BillingPlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingPlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_billing_plan, parent, false)
        return BillingPlanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plans.count()
    }

    override fun onBindViewHolder(holder: BillingPlanViewHolder, position: Int) {
        val item = plans[position]

        holder.bindView(item, listener)
    }
}