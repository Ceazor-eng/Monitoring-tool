package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.CompleteMonthlyTransactionsEntity
import com.monitoringtendepay.domain.models.CompleteTransactions

data class TotalMonthlyTransactionsDto(
    val completePayments: String,
    val timestamp: Long // Add a timestamp field
)

fun TotalMonthlyTransactionsDto.toEntity(timestamp: Long): CompleteMonthlyTransactionsEntity {
    return CompleteMonthlyTransactionsEntity(
        completePayments,
        timestamp
    )
}

fun TotalMonthlyTransactionsDto.toCompleteTransactions(): CompleteTransactions {
    return CompleteTransactions(
        completePayments
    )
}