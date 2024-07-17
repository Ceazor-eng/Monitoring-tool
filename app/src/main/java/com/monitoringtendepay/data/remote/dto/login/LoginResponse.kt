package com.monitoringtendepay.data.remote.dto.login

data class LoginResponse(
    val status: String,
    val message: String,
    val role: String,
    val username: String,
    val salutation: String,
    val sessionToken: String
)