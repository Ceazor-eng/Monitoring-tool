package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.FetchAllPaymentsDto
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import javax.inject.Inject

class AllPaymentsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : AllPaymentsRepository {
    override suspend fun getAllPayments(action: String): List<FetchAllPaymentsDto> {
        return api.getAllPayments(action)
    }
}