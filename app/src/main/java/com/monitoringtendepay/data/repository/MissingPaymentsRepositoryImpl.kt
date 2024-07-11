package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.MissingPaymentsDto
import com.monitoringtendepay.domain.repository.MissingPaymentsRepository
import javax.inject.Inject

class MissingPaymentsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : MissingPaymentsRepository {
    override suspend fun getMissingTransactions(action: String): MissingPaymentsDto {
        val response = api.getMissingPayments(action)
        return response
    }
}