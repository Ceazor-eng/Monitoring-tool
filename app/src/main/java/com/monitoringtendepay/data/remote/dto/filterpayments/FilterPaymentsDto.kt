package com.monitoringtendepay.data.remote.dto.filterpayments

import com.monitoringtendepay.domain.models.PaymentsFilterParams

data class FilterPaymentsDto(
    val Id: String,
    val amount: String,
    val groupId: String,
    val initiatorName: String,
    val initiatorPhone: String,
    val internalRef: String,
    val mpesaRef: String,
    val paymentStatus: String,
    val paymentStatusMessage: String,
    val salesforcePhone: String,
    val serviceCode: String,
    val sessionId: String,
    val transactionDate: String
)

fun FilterPaymentsDto.toFilteredPayments(): PaymentsFilterParams {
    return PaymentsFilterParams(
        paymentStatus,
        serviceCode,
        paymentStatusMessage,
        initiatorName,
        groupId,
        initiatorPhone,
        salesforcePhone,
        sessionId,
        mpesaRef,
        internalRef,
        amount,
        transactionDate
    )
}