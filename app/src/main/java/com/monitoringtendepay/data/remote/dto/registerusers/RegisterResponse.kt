package com.monitoringtendepay.data.remote.dto.registerusers

data class RegisterResponse(
    val message: String,
    val otp: Int,
    val salutation: String,
    val status: String,
    val username: String
)