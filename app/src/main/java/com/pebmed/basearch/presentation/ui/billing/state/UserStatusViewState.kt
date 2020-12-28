package com.pebmed.basearch.presentation.ui.billing.state

import br.com.pebmed.domain.entities.billing.PurchasedPlanModel

sealed class UserStatusViewState {
    class PremiumUser(val purchasedPlanModel: PurchasedPlanModel) : UserStatusViewState()

    object FreeUser : UserStatusViewState()
}