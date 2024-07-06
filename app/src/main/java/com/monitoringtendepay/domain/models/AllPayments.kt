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
// Service Code	Initiator Phone	Amount	Mpesa Ref	Recipient Name	Session Id	Payment Status	Date and Time