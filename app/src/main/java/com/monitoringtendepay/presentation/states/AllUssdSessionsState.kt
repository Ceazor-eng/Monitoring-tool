package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.AllUssdSessions

data class AllUssdSessionsState(
    val isLoading: Boolean = false,
    val ussdSessions: List<AllUssdSessions> = emptyList(),
    val error: String = ""
)