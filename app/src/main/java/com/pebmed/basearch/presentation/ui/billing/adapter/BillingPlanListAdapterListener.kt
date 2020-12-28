package com.pebmed.basearch.presentation.ui.billing.adapter

import br.com.pebmed.domain.entities.billing.PlanModel

interface BillingPlanListAdapterListener {
    fun onItemClick(plan: PlanModel)
}