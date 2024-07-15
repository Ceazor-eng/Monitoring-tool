package com.monitoringtendepay.data.remote.dto.login

import com.monitoringtendepay.domain.models.LoginUser

data class LoginResponseDto(
    val message: String,
    val role: String,
    val salutation: String,
    val sessionToken: String,
    val status: String,
    val username: String
)

fun LoginResponseDto.toLoginUser(): LoginUser {
    return LoginUser(
        message,
        role,
        salutation,
        sessionToken,
        status,
        username
    )
}