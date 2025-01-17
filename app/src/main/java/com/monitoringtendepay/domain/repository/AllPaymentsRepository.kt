package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.payments.FetchAllPaymentsDto

interface AllPaymentsRepository {
    suspend fun getAllPayments(action: String): List<FetchAllPaymentsDto>
}