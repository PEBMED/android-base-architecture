package com.pebmed.basearch.presentation.ui.billing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.pebmed.basearch.R
import com.pebmed.basearch.presentation.extensions.showToast
import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import com.pebmed.platform.billing.GooglePlayBillingResponseCodeModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BillingActivity : AppCompatActivity() {
    private val googlePlayBillingClient by inject<GooglePlayBillingClientWrapper>()
    private val viewModel by viewModel<BillingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        lifecycle.addObserver(googlePlayBillingClient)

        googlePlayBillingClient.connectionStatusEvent.observe(this, Observer {
            viewModel.loadActivePlans()

            googlePlayBillingClient.connectionStatusEvent.removeObservers(this)
        })

        googlePlayBillingClient.purchaseUpdateEvent.observe(this, Observer {
            it.success?.let { purchases ->
                val purchasedPlan = purchases.firstOrNull{ purchase -> purchase.productId == viewModel.planUnderPurchase?.id}

                if (purchasedPlan != null) {
                    viewModel.onGooglePlayPlanPurchaseSuccess(purchasedPlan)
                } else {
                    showError()
                }
            } ?: run {
                when {
                    it.error != GooglePlayBillingResponseCodeModel.USER_CANCELED -> showError()
                }
            }
        })
    }

    private fun subscribe(id: String) {
        val skuDetails = viewModel.googlePlayPlansMap[id]
        skuDetails?.let {
            googlePlayBillingClient.launchBillingFlow(this, it)
        } ?: run {
            showError()
        }
    }

    private fun showError() {
        showToast("Não foi possível realizar a compra. Por favor, entre em contato com o suporte")
    }
}
