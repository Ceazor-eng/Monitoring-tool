package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.PaymentsFilterParams

data class FilteredPaymentsState(
    val isLoading: Boolean = false,
    val filterPayments: List<PaymentsFilterParams> = emptyList(),
    val error: String = ""
)