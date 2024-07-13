package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.filterpayments.FilterPaymentsDto

interface FilterPaymentsRepository {
    suspend fun filterPayments(action: String, serviceType: String, status: String, startDate: String, endDate: String): List<FilterPaymentsDto>
}