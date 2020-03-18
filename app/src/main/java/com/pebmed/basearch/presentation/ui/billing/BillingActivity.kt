package com.pebmed.basearch.presentation.ui.billing

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.entities.billing.*
import com.pebmed.basearch.BuildConfig
import com.pebmed.basearch.R
import com.pebmed.basearch.presentation.extensions.*
import com.pebmed.basearch.presentation.ui.base.ViewState
import com.pebmed.basearch.presentation.ui.billing.adapter.BillingPlanListAdapter
import com.pebmed.basearch.presentation.ui.billing.adapter.BillingPlanListAdapterListener
import com.pebmed.basearch.presentation.ui.billing.state.PlansViewState
import com.pebmed.basearch.presentation.ui.billing.state.UserStatusViewState
import com.pebmed.platform.billing.GooglePlayBillingClientWrapper
import com.pebmed.platform.billing.GooglePlayBillingResponseCodeModel
import kotlinx.android.synthetic.main.activity_billing.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BillingActivity : AppCompatActivity() {
    private val googlePlayBillingClient by inject<GooglePlayBillingClientWrapper>()
    private val viewModel by viewModel<BillingViewModel>()

    private val dialogTitle = "PEBMED"
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        initToolbar()
        initBilling()
        initObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Billing"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initBilling() {
        lifecycle.addObserver(googlePlayBillingClient)

        googlePlayBillingClient.connectionStatusEvent.observe(this, Observer {
            viewModel.fetchUserStatus()

            googlePlayBillingClient.connectionStatusEvent.removeObservers(this)
        })

        googlePlayBillingClient.purchaseUpdateEvent.observe(this, Observer {
            it.success?.let { purchases ->
                val purchasedPlan = purchases.firstOrNull { purchase -> purchase.productId == viewModel.planUnderPurchase?.storeId }

                if (purchasedPlan != null) {
                    showServerResponseTypeDialog(purchasedPlan)
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

    private fun initObservers() {
        viewModel.userStatusViewState.observe(this, Observer {
            when (it) {
                is UserStatusViewState.PremiumUser -> showUserPremiumView(it.purchasedPlanModel)

                UserStatusViewState.FreeUser -> {
                    showUserFreeView()

                    viewModel.loadActivePlans()
                }
            }
        })

        viewModel.plansViewState.observe(this, Observer {
            when (it) {
                PlansViewState.Loading -> showLoading("Buscando planos...")
                is PlansViewState.Success -> showPlans(it.plans)
                is PlansViewState.Error -> showError(it.baseErrorData)
                is PlansViewState.PendingValidation -> showPendingSubscriptionValidationDialog(it.pendingSubscriptionValidationModel)
            }
        })

        viewModel.purchaseViewState.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    dismissLoading()
                    showToast("Compra realizada com sucesso")
                    viewModel.fetchUserStatus()
                }
                is ViewState.Error -> showError(it.error)
                is ViewState.Loading -> showLoading("Validando compra...")
            }
        })
    }

    private fun subscribeGooglePlayPlan(id: String) {
        val skuDetails = viewModel.googlePlayPlansMap[id]
        skuDetails?.let {
            googlePlayBillingClient.launchBillingFlow(this, it)
        } ?: run {
            showError()
        }
    }

    private fun showPlans(plans: List<PlanModel>) {
        dismissLoading()

        layoutBillingFreeUser.setVisible()
        layoutBillingPremiumUser.setGone()
        layoutBillingError.setGone()

        val adapter = BillingPlanListAdapter(plans, object : BillingPlanListAdapterListener {
            override fun onItemClick(plan: PlanModel) {
                when (plan.gateway) {
                    PlanGateway.GOOGLE_PLAY -> {
                        viewModel.planUnderPurchase = plan

                        if (BuildConfig.DEBUG) {
                            val fakePurchasedPlan = GooglePlayPurchasedPlanModel(
                                "fake_order_id",
                                plan.storeId,
                                "fake_purchase_token"
                            )
                            showServerResponseTypeDialog(fakePurchasedPlan)
                        } else {
                            subscribeGooglePlayPlan(plan.storeId)
                        }
                    }
                    PlanGateway.OTHER -> {
                        showToast("Método de pagamento não implementado")
                    }
                }
            }
        })
        recyclerViewBillingPlans.layoutManager = LinearLayoutManager(this)
        recyclerViewBillingPlans.adapter = adapter
    }

    private fun showUserPremiumView(purchasedPlanModel: PurchasedPlanModel) {
        layoutBillingFreeUser.setGone()
        layoutBillingPremiumUser.setVisible()
        layoutBillingError.setGone()

        textBillingUserStatus.text = "Premium"

        textBillingPlanTitle.text = purchasedPlanModel.plan.title
        textBillingPlanStoreId.text = purchasedPlanModel.plan.storeId
        buttonBillingCancelPlan.setOnClickListener { viewModel.removeSubscription() }
    }

    private fun showUserFreeView() {
        textBillingUserStatus.text = "Free"
    }

    private fun showLoading(message: String) {
        progressDialog = showProgress(message)
    }

    private fun dismissLoading() {
        progressDialog?.dismiss()
    }

    private fun showError(baseError: BaseErrorData<BaseErrorStatus>? = null) {
        dismissLoading()

        textBillingError.text = baseError?.errorMessage ?: "Ocorreu um erro ao realizar a operacao."
        buttonBillingTryAgain.setOnClickListener { viewModel.loadActivePlans() }

        layoutBillingFreeUser.setGone()
        layoutBillingPremiumUser.setGone()
        layoutBillingError.setVisible()
    }

    private fun showServerResponseTypeDialog(googlePlayPurchasedPlanModel: GooglePlayPurchasedPlanModel) {
        val items = arrayOf("Sucesso", "Erro")
        showRadioButtonSelectionDialog("Escolha a resposta do servidor", items) {
            val forceError = it != 0
            viewModel.onGooglePlayPlanPurchaseSuccess(googlePlayPurchasedPlanModel, forceError)
        }
    }

    private fun showPendingSubscriptionValidationDialog(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) {
        dismissLoading()

        when(pendingSubscriptionValidationModel.type) {
            PendingSubscriptionValidationType.GOOGLE_PLAY_ACKNOWLEDGE -> showPendingAcknowledgePurchaseDialog(pendingSubscriptionValidationModel)
            PendingSubscriptionValidationType.BACKEND -> showPendingServerSyncDialog(pendingSubscriptionValidationModel)
        }
    }

    private fun showPendingServerSyncDialog(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) {
        showAlert(dialogTitle, "Sua compra está pendente de sincronizacao com nossos servidores.", "OK", DialogInterface.OnClickListener { dialog, which ->
            viewModel.validatePendingServerSyncSubscription(pendingSubscriptionValidationModel)
        })
    }

    private fun showPendingAcknowledgePurchaseDialog(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel) {
        showAlert(dialogTitle, "Sua compra está pendente de reconhecimento na Google Play.", "OK", DialogInterface.OnClickListener { dialog, which ->
            viewModel.validatePendingAcknowledgePurchase(pendingSubscriptionValidationModel)
        })
    }
}
