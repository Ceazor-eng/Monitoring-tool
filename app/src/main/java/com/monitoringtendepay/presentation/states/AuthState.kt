package com.monitoringtendepay.presentation.states

data class AuthState(
    val isLoading: Boolean = false,
    val data: String? = "",
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)