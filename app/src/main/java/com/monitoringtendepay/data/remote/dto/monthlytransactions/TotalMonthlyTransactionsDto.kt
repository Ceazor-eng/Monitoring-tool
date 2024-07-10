package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.domain.models.CompleteTransactions

data class TotalMonthlyTransactionsDto(
    val completePayments: String
)

fun TotalMonthlyTransactionsDto.toCompleteTransactions(): CompleteTransactions {
    return CompleteTransactions(
        completePayments
    )
}