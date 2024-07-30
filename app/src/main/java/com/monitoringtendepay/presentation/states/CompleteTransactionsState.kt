package com.monitoringtendepay.presentation.states

import com.monitoringtendepay.domain.models.CompleteTransactions

data class CompleteTransactionsState(
    val isLoading: Boolean = false,
    val completeTransactions: CompleteTransactions? = null,
    val error: String = ""
)