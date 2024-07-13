package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.filterussdsessions.FilterUssdSessionsDto

interface FilterUssdSessionsRepository {
    suspend fun filterUssdSessions(action: String, phoneNumber: String, sessionId: String, startDate: String, endDate: String): List<FilterUssdSessionsDto>
}