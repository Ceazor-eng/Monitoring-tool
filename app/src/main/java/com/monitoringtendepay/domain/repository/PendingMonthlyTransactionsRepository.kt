package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.monthlytransactions.PendingMonthlyTransactionsDto

interface PendingMonthlyTransactionsRepository {
    suspend fun getPendingTransactions(action: String): PendingMonthlyTransactionsDto
}