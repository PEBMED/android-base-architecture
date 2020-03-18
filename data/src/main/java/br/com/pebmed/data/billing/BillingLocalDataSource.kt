package br.com.pebmed.data.billing

import androidx.core.content.edit
import br.com.pebmed.data.base.SharedPreferencesUtil
import br.com.pebmed.data.base.getClass
import br.com.pebmed.data.base.putClass
import br.com.pebmed.domain.entities.billing.PendingSubscriptionValidationModel
import br.com.pebmed.domain.entities.billing.PurchasedPlanModel

class BillingLocalDataSource(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) {
    fun setPendingSubscriptionValidation(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel?) {
        sharedPreferencesUtil.prefs.edit {
            putClass(SharedPreferencesUtil.PREF_PENDING_SUBSCRIPTION_VALIDATION, pendingSubscriptionValidationModel)
        }
    }

    fun getPendingSubscriptionValidation(): PendingSubscriptionValidationModel? {
        return sharedPreferencesUtil.prefs.getClass(
            SharedPreferencesUtil.PREF_PENDING_SUBSCRIPTION_VALIDATION,
            PendingSubscriptionValidationModel::class.java
        )
    }

    fun getSavedPurchasedPlan(): PurchasedPlanModel? {
        return sharedPreferencesUtil.prefs.getClass(
            SharedPreferencesUtil.PREF_PURCHASED_PLAN,
            PurchasedPlanModel::class.java
        )
    }

    fun savePurchasedPlan(purchasedPlanModel: PurchasedPlanModel?) {
        sharedPreferencesUtil.prefs.edit{
            putClass(SharedPreferencesUtil.PREF_PURCHASED_PLAN, purchasedPlanModel)
        }
    }
}