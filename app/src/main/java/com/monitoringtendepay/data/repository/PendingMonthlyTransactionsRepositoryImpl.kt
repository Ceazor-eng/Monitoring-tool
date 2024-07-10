package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.PendingMonthlyTransactionsDto
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import javax.inject.Inject

class PendingMonthlyTransactionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : PendingMonthlyTransactionsRepository {
    override suspend fun getPendingTransactions(action: String): PendingMonthlyTransactionsDto {
        val response = api.getPendingMonthlyTransactions(action)
        return response
    }
}