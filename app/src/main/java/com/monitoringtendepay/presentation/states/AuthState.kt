package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.models.RegisterUser

data class LoginState(
    val isLoading: Boolean = false,
    val data: LoginUser? = null,
    val error: String? = null,
    val changePasswordRequired: Boolean = false
)

data class AuthState(
    val isLoading: Boolean = false,
    val data: String? = null,
    val error: String? = null
)

data class RegisterState(
    val isLoading: Boolean = false,
    val data: RegisterUser? = null,
    val error: String? = null
)