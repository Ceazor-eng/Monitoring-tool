package com.monitoringtendepay.presentation.states

data class CompleteTransactionsState(
    val isLoading: Boolean = false,
    val completeTransactions: String? = null,
    val error: String = ""
)