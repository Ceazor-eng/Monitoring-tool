package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.FilterUssdSessionsParams

data class FilteredUssdSessionsState(
    val isLoading: Boolean = false,
    val filterUssdSessions: List<FilterUssdSessionsParams> = emptyList(),
    val error: String = ""
)