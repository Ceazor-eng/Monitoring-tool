package com.monitoringtendepay.domain.models

data class RegisterUser(
    val message: String,
    val otp: Int,
    val salutation: String,
    val status: String,
    val username: String
)