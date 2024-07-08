package com.monitoringtendepay.data.remote.dto.ussd

import com.monitoringtendepay.domain.models.AllUssdSessions

data class UssdData(
    val msisdn: String,
    val sessionId: String,
    val sessionsDate: String
)

fun UssdData.toAllUssdSessions() : AllUssdSessions{
    return AllUssdSessions(
        msisdn,
        sessionId,
        sessionsDate
    )
}