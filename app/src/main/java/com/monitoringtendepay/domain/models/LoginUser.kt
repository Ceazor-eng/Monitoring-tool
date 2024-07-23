package com.monitoringtendepay.domain.models

data class LoginUser(
    val status: String,
    val message: String,
    val role: String,
    val username: String?,
    val salutation: String?,
    val sessionToken: String
)