package com.pebmed.platform.billing

import br.com.pebmed.domain.entities.billing.GooglePlayPurchasedPlanModel
import br.com.pebmed.domain.entities.billing.PlanGateway
import br.com.pebmed.domain.entities.billing.PlanModel
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

fun SkuDetails.toPlanModel(): PlanModel {
    return PlanModel(
        id = "-1",
        storeId = sku,
        title = title,
        price = price,
        gateway = PlanGateway.GOOGLE_PLAY,
        customMessage = ""
    )
}

fun Purchase.toDomainReceipt(): GooglePlayPurchasedPlanModel {
    return GooglePlayPurchasedPlanModel(
        orderId = this.orderId,
        productId = this.sku,
        purchaseToken = this.purchaseToken
    )
}