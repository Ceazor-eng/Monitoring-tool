package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.filterpayments.FilterPaymentsDto
import com.monitoringtendepay.domain.models.PaymentsFilterParams

interface FilterPaymentsRepository {
    suspend fun filterPayments(action: String, params: PaymentsFilterParams): List<FilterPaymentsDto>
}