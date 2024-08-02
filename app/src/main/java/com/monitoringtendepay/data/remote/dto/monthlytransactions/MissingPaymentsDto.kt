package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.MissingTransactionsEntity
import com.monitoringtendepay.domain.models.MissingPayments

data class MissingPaymentsDto(
    val missingPayments: String,
    val timestamp: Long // Add a timestamp field
)
fun MissingPaymentsDto.toEntity(timestamp: Long): MissingTransactionsEntity {
    return MissingTransactionsEntity(
        missingPayments,
        timestamp
    )
}

fun MissingPaymentsDto.toMissingPayments(): MissingPayments {
    return MissingPayments(
        missingPayments
    )
}