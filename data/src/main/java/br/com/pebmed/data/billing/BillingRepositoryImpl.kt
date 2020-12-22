package br.com.pebmed.data.billing

import br.com.pebmed.domain.base.BaseErrorData
import br.com.pebmed.domain.base.BaseErrorStatus
import br.com.pebmed.domain.base.ResultWrapper
import br.com.pebmed.domain.entities.billing.*
import br.com.pebmed.domain.extensions.SupportedDateFormat
import br.com.pebmed.domain.extensions.toSupportedDateFormat
import br.com.pebmed.domain.repository.BillingRepository
import kotlinx.coroutines.delay
import java.util.*

class BillingRepositoryImpl(
    private val localDataSource: BillingLocalDataSource
) : BillingRepository {
    override suspend fun validatePurchasedStorePlan(
        purchasedPlanModel: GooglePlayPurchasedPlanModel,
        forceError: Boolean
    ): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>> {
        delay(1000)

        //Fake server response
        return if (forceError) {
            ResultWrapper(error = BaseErrorData(errorBody = BaseErrorStatus.DEFAULT_ERROR))
        } else {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, 1)
            val endDate = calendar.time.toSupportedDateFormat(SupportedDateFormat.SERVER)

            ResultWrapper(
                success = PurchasedPlanModel(
                    plan = PlanModel(
                        id = "fake_id",
                        storeId = purchasedPlanModel.productId,
                        title = "fake_title",
                        price = "fake_price",
                        gateway = PlanGateway.GOOGLE_PLAY,
                        customMessage = "fake_custom_message"
                    ),
                    active = true,
                    endDate = endDate
                )
            )
        }
    }

    override fun setPendingSubscriptionValidation(pendingSubscriptionValidationModel: PendingSubscriptionValidationModel?) {
        localDataSource.setPendingSubscriptionValidation(pendingSubscriptionValidationModel)
    }

    override fun getPendingSubscriptionValidation(): ResultWrapper<PendingSubscriptionValidationModel, BaseErrorData<BaseErrorStatus>> {
        val pendingSubscriptionValidationModel = localDataSource.getPendingSubscriptionValidation()

        pendingSubscriptionValidationModel?.let {
            return ResultWrapper(success = it)
        } ?: run {
            return ResultWrapper(error = BaseErrorData(errorBody = BaseErrorStatus.DATA_NOT_FOUND))
        }
    }

    override fun getSavedPurchasedPlan(): ResultWrapper<PurchasedPlanModel, BaseErrorData<BaseErrorStatus>> {
        val purchasedPlanModel = localDataSource.getSavedPurchasedPlan()

        purchasedPlanModel?.let {
            return ResultWrapper(success = it)
        } ?: run {
            return ResultWrapper(error = BaseErrorData(errorBody = BaseErrorStatus.DATA_NOT_FOUND))
        }
    }

    override fun savePurchasedPlan(purchasedPlanModel: PurchasedPlanModel?) {
        localDataSource.savePurchasedPlan(purchasedPlanModel)
    }

    override suspend fun getFakePlans(): List<PlanModel> {
        delay(1000)

        val plans = arrayListOf<PlanModel>()

        plans.add(
            PlanModel(
                id = "0",
                storeId = "plano_gateway_blablabla_anual",
                title = "Titulo Anual",
                price = "R$500",
                gateway = PlanGateway.OTHER,
                customMessage = "Mensagem customizada anual"
            )
        )

        plans.add(
            PlanModel(
                id = "1",
                storeId = PlanCode.ANNUAL,
                title = "Titulo Anual Google Play",
                price = "R$50",
                gateway = PlanGateway.GOOGLE_PLAY,
                customMessage = "Mensagem customizada google play"
            )
        )

        plans.add(
            PlanModel(
                id = "2",
                storeId = "plano_gateway_blablabla_mensal",
                title = "Titulo Mensal",
                price = "R$30",
                gateway = PlanGateway.OTHER,
                customMessage = "Mensagem customizada mensal"
            )
        )

        return plans
    }
}