package com.monitoringtendepay.data.remote.dto.payments

data class Paymentss(
    val `data`: List<FetchAllPaymentsDto>,
    val totalPayments: String
)