package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.FailedTransactions

data class FailedTransactionState(
    val isLoading: Boolean = false,
    val failedTransactions: FailedTransactions? = null,
    val error: String = ""
)