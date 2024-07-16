package com.monitoringtendepay.domain.models

data class LoginUser(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)