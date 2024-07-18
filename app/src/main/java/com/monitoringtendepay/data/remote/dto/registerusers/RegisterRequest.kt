package com.monitoringtendepay.data.remote.dto.registerusers

data class RegisterRequest(
    val action: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val roleID: String,
    val username: String
)