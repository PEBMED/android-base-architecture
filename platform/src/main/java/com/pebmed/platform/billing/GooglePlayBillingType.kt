package com.pebmed.platform.billing

import com.android.billingclient.api.BillingClient

enum class GooglePlayBillingType(val value: String) {
    SUBS(BillingClient.SkuType.SUBS),
    INAPP(BillingClient.SkuType.INAPP)
}