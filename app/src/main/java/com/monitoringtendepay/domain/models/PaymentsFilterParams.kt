package com.monitoringtendepay.domain.models

data class PaymentsFilterParams(
    val paymentStatus: String,
    val serviceCode: String,
    val transactionDate: String
)