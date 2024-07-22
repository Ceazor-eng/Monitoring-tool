package com.monitoringtendepay.data.remote.dto.registerusers

data class RegisterResponse(
    val data: RegisterData
)

data class RegisterData(
    val message: String,
    val otp: Int,
    val salutation: String,
    val status: String,
    val username: String
)