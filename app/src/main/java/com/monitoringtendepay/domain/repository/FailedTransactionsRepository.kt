package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.monthlytransactions.FailedMonthlyTransactionsDto

interface FailedTransactionsRepository {
    suspend fun getFailedTransactions(action: String): FailedMonthlyTransactionsDto
}