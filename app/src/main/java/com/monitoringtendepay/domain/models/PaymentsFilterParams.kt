package com.monitoringtendepay.domain.models

data class PaymentsFilterParams(
    val paymentStatus: String,
    val serviceCode: String,
    val paymentStatusMessage: String,
    val initiatorName: String,
    val groupId: String,
    val initiatorPhone: String,
    val salesforcePhone: String,
    val sessionId: String,
    val mpesaRef: String,
    val internalRef: String,
    val amount: String,
    val transactionDate: String
)