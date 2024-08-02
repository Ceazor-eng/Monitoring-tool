package com.monitoringtendepay.data.remote.dto.payments

import com.monitoringtendepay.data.localdatasource.AllPaymentsEntity
import com.monitoringtendepay.domain.models.AllPayments

data class FetchAllPaymentsDto(
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

fun FetchAllPaymentsDto.toEntity(): AllPaymentsEntity {
    return AllPaymentsEntity(
        Id,
        amount,
        groupId,
        initiatorName,
        initiatorPhone,
        internalRef,
        mpesaRef,
        paymentStatus,
        paymentStatusMessage,
        salesforcePhone,
        serviceCode,
        sessionId,
        transactionDate
    )
}

fun FetchAllPaymentsDto.toAllPayments(): AllPayments {
    return AllPayments(
        amount,
        initiatorPhone,
        mpesaRef,
        paymentStatus,
        serviceCode,
        sessionId,
        transactionDate
    )
}