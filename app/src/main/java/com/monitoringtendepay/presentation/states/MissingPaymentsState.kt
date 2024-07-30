package com.monitoringtendepay.presentation.states

data class MissingPaymentsState(
    val isLoading: Boolean = false,
    val missingPayments: String? = null,
    val error: String = ""
)