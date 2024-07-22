package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.data.remote.dto.allusers.UserDetails

data class UsersState(
    val isLoading: Boolean = false,
    val users: List<UserDetails> = emptyList(),
    val error: String = ""
)