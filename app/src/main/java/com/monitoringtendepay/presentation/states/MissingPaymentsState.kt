package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.MissingPayments

data class MissingPaymentsState(
    val isLoading: Boolean = false,
    val missingPayments: MissingPayments? = null,
    val error: String = ""
)