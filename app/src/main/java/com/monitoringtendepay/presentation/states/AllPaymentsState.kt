package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.AllPayments

data class AllPaymentsState(
    val isLoading: Boolean = false,
    val payments: List<AllPayments> = emptyList(),
    val error: String = ""
)