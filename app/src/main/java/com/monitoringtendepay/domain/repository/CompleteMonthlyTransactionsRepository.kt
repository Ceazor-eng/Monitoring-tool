package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.monthlytransactions.TotalMonthlyTransactionsDto

interface CompleteMonthlyTransactionsRepository {
    suspend fun getCompleteTransactions(action: String): TotalMonthlyTransactionsDto
}