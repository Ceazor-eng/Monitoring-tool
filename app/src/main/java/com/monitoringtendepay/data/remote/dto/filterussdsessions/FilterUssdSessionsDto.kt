package com.monitoringtendepay.data.remote.dto.filterussdsessions

import com.monitoringtendepay.domain.models.FilterUssdSessionsParams

data class FilterUssdSessionsDto(
    val msisdn: String,
    val sessionId: String,
    val sessionsDate: String
)

fun FilterUssdSessionsDto.toFilteredUssdSessions(): FilterUssdSessionsParams {
    return FilterUssdSessionsParams(
        msisdn,
        sessionId,
        sessionsDate
    )
}