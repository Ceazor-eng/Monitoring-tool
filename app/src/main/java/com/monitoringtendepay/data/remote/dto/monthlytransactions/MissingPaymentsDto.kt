package com.monitoringtendepay.data.remote.dto.monthlytransactions

import com.monitoringtendepay.domain.models.MissingPayments

data class MissingPaymentsDto(
    val missingPayments: String
)
fun MissingPaymentsDto.toMissingPayments(): MissingPayments {
    return MissingPayments(
        missingPayments
    )
}