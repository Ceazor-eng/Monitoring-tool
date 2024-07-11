package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.monthlytransactions.MissingPaymentsDto

interface MissingPaymentsRepository {
    suspend fun getMissingTransactions(action: String): MissingPaymentsDto
}