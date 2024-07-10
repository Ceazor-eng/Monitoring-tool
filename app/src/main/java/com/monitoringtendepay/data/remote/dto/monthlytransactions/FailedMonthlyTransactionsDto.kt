package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.domain.models.FailedTransactions

data class FailedMonthlyTransactionsDto(
    val failedPayments: String
)

fun FailedMonthlyTransactionsDto.toFailedTransactions(): FailedTransactions {
    return FailedTransactions(
        failedPayments
    )
}