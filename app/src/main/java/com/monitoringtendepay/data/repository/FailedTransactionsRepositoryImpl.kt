package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.FailedMonthlyTransactionsDto
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import javax.inject.Inject

class FailedTransactionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : FailedTransactionsRepository {
    override suspend fun getFailedTransactions(action: String): FailedMonthlyTransactionsDto {
        val response = api.getFailedMonthlyTransactions(action)
        return response
    }
}