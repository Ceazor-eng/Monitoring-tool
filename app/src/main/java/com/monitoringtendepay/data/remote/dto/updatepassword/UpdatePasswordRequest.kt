package com.monitoringtendepay.data.remote.dto.updatepassword

data class UpdatePasswordRequest(
    val action: String,
    val username: String,
    val password: String
)