package com.pebmed.basearch.presentation.ui.billing.state

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.billing.PendingSubscriptionValidationModel
import br.com.pebmed.domain.entities.billing.PlanModel

sealed class PlansViewState {
    object Loading : PlansViewState()

    class Success(val plans: List<PlanModel>) : PlansViewState()

    object Empty : PlansViewState()

    class Error(val baseErrorData: BaseErrorData<BaseErrorStatus>) : PlansViewState()

    class PendingValidation(val pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) : PlansViewState()
}