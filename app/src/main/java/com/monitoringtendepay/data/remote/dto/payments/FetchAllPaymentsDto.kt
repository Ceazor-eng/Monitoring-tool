package com.monitoringtendepay.data.remote.dto.payments

import com.monitoringtendepay.data.localdatasource.allpaymentslocaldatabase.AllPaymentsEntity
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
    val transactionDate: String,
    val timestamp: Long // Add a timestamp field
)

fun FetchAllPaymentsDto.toEntity(timestamp: Long): AllPaymentsEntity {
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
        transactionDate,
        timestamp
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