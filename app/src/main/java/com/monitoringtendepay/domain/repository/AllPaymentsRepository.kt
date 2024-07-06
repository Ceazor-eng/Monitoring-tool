package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.FetchAllPaymentsDto

interface AllPaymentsRepository {
    suspend fun getAllPayments(action: String): List<FetchAllPaymentsDto>
}