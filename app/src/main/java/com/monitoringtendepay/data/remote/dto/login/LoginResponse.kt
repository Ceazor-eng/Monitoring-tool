package com.monitoringtendepay.data.remote.dto.login

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String,
    val name: String
)