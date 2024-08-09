package com.monitoringtendepay.domain.models

data class AllPayments(
    val amount: String,
    val initiatorPhone: String,
    val mpesaRef: String,
    val paymentStatus: String,
    val serviceCode: String,
    val sessionId: String,
    val transactionDate: String
)