package com.monitoringtendepay.data.remote.dto.filterpayments

data class FilteredPayments(
    val `data`: List<FilterPaymentsDto>,
    val totalTransactions: String
)