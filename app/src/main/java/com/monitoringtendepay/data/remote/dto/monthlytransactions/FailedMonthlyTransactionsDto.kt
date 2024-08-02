package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.FailedTransactionsEntity
import com.monitoringtendepay.domain.models.FailedTransactions

data class FailedMonthlyTransactionsDto(
    val failedPayments: String,
    val timestamp: Long // Add a timestamp field
)

fun FailedMonthlyTransactionsDto.toEntity(timestamp: Long): FailedTransactionsEntity {
    return FailedTransactionsEntity(
        failedPayments,
        timestamp
    )
}

fun FailedMonthlyTransactionsDto.toFailedTransactions(): FailedTransactions {
    return FailedTransactions(
        failedPayments
    )
}