package com.monitoringtendepay.domain.models

data class LoginUser(
    val message: String,
    val role: String,
    val salutation: String,
    val sessionToken: String,
    val status: String,
    val username: String
)