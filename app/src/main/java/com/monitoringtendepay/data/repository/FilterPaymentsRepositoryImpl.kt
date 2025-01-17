package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.filterpayments.FilterPaymentsDto
import com.monitoringtendepay.domain.repository.FilterPaymentsRepository
import javax.inject.Inject

class FilterPaymentsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : FilterPaymentsRepository {
    override suspend fun filterPayments(
        action: String,
        serviceType: String,
        status: String,
        startDate: String,
        endDate: String
    ): List<FilterPaymentsDto> {
        val response = api.filterPayments(action, serviceType, status, startDate, endDate)
        return response.data
    }
}