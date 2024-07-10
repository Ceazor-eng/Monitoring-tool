package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.domain.models.PendingTransactions

data class PendingMonthlyTransactionsDto(
    val pendingPayments: String
)

fun PendingMonthlyTransactionsDto.toPendingMonthlyTransactions(): PendingTransactions {
    return PendingTransactions(
        pendingPayments
    )
}