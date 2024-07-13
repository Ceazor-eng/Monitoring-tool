package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.filterussdsessions.FilterUssdSessionsDto
import com.monitoringtendepay.domain.repository.FilterUssdSessionsRepository
import javax.inject.Inject

class FilterUssdSessionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : FilterUssdSessionsRepository {
    override suspend fun filterUssdSessions(action: String, phoneNumber: String, sessionId: String, startDate: String, endDate: String): List<FilterUssdSessionsDto> {
        val response = api.filterUssdSessions(action, phoneNumber, sessionId, startDate, endDate)
        return response.data
    }
}