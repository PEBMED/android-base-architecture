package com.pebmed.platform.billing

enum class GooglePlayBillingResponseCodeModel {
    OK,
    SERVICE_TIMEOUT,
    FEATURE_NOT_SUPPORTED,
    SERVICE_DISCONNECTED,
    USER_CANCELED,
    SERVICE_UNAVAILABLE,
    BILLING_UNAVAILABLE,
    ITEM_UNAVAILABLE,
    DEVELOPER_ERROR,
    ERROR,
    ITEM_ALREADY_OWNED,
    ITEM_NOT_OWNED,
    UNKNOWN
}

fun GooglePlayBillingResponseCodeModel.isSuccess(): Boolean = when (this) {
    GooglePlayBillingResponseCodeModel.OK -> true
    GooglePlayBillingResponseCodeModel.SERVICE_TIMEOUT,
    GooglePlayBillingResponseCodeModel.FEATURE_NOT_SUPPORTED,
    GooglePlayBillingResponseCodeModel.SERVICE_DISCONNECTED,
    GooglePlayBillingResponseCodeModel.USER_CANCELED,
    GooglePlayBillingResponseCodeModel.SERVICE_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.BILLING_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.ITEM_UNAVAILABLE,
    GooglePlayBillingResponseCodeModel.DEVELOPER_ERROR,
    GooglePlayBillingResponseCodeModel.ERROR,
    GooglePlayBillingResponseCodeModel.ITEM_ALREADY_OWNED,
    GooglePlayBillingResponseCodeModel.ITEM_NOT_OWNED,
    GooglePlayBillingResponseCodeModel.UNKNOWN -> false
}