package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.TotalMonthlyTransactionsDto
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import javax.inject.Inject

class CompleteMonthlyTransactionsImpl @Inject constructor(
    private val api: AllPaymentsApi
) : CompleteMonthlyTransactionsRepository {
    override suspend fun getCompleteTransactions(action: String): TotalMonthlyTransactionsDto {
        val response = api.getTotalMonthlyTransactions(action)
        return response
    }
}