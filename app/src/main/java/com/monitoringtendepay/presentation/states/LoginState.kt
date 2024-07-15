package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.LoginUser

data class LoginState(
    val isLoading: Boolean = false,
    val success: List<LoginUser>? = null,
    val error: String = ""
)