package com.monitoringtendepay.data.remote.dto.login

data class LoginRequest(
    val action: String,
    val username: String,
    val password: String
)