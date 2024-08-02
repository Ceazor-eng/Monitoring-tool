package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.PendingTransactionsEntity
import com.monitoringtendepay.domain.models.PendingTransactions

data class PendingMonthlyTransactionsDto(
    val pendingPayments: String,
    val timestamp: Long // Add a timestamp field
)

fun PendingMonthlyTransactionsDto.toEntity(timestamp: Long): PendingTransactionsEntity {
    return PendingTransactionsEntity(
        pendingPayments,
        timestamp
    )
}

fun PendingMonthlyTransactionsDto.toPendingMonthlyTransactions(): PendingTransactions {
    return PendingTransactions(
        pendingPayments
    )
}