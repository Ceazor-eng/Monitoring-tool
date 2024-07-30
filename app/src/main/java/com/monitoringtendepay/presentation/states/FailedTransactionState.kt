package com.monitoringtendepay.presentation.states

data class FailedTransactionState(
    val isLoading: Boolean = false,
    val failedTransactions: String? = null,
    val error: String = ""
)