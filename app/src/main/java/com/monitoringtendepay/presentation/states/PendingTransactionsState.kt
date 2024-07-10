package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.PendingTransactions

data class PendingTransactionsState(
    val isLoading: Boolean = false,
    val pendingTransactions: PendingTransactions? = null,
    val error: String = ""
)