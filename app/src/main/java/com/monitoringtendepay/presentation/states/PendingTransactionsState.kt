package com.monitoringtendepay.presentation.states

data class PendingTransactionsState(
    val isLoading: Boolean = false,
    val pendingTransactions: String? = null,
    val error: String = ""
)