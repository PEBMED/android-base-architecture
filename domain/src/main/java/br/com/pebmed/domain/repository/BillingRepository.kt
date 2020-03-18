package br.com.pebmed.domain.repository

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.billing.GooglePlayPurchasedPlanModel
import br.com.pebmed.domain.entities.billing.PendingSubscriptionValidationModel
import br.com.pebmed.domain.entities.billing.PlanModel
import br.com.pebmed.domain.entities.billing.PurchasedPlanModel

interface BillingRepository {
    suspend fun validatePurchasedStorePlan(
        purchasedPlanModel: GooglePlayPurchasedPlanModel,
        forceError: Boolean = false
    ): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>>

    fun setPendingSubscriptionValidation(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel?)

    fun getPendingSubscriptionValidation(): ResultWrapper<PendingSubscriptionValidationModel, BaseErrorData<BaseErrorStatus>>

    fun getSavedPurchasedPlan(): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>>

    fun savePurchasedPlan(purchasedPlanModel: PurchasedPlanModel?)

    suspend fun getFakePlans(): List<PlanModel>
}