package com.monitoringtendepay.data.remote.dto

data class Paymentss(
    val `data`: List<FetchAllPaymentsDto>,
    val totalPayments: String
)