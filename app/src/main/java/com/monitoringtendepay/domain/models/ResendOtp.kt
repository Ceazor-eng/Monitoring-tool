package com.monitoringtendepay.domain.models

data class ResendOtp(
    val message: String,
    val newOtp: Int,
    val phoneNumber: String,
    val status: String,
    val username: String
)