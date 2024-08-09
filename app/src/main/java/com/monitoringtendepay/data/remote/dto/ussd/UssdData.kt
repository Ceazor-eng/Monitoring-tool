package com.monitoringtendepay.data.remote.dto.ussd

import com.monitoringtendepay.data.localdatasource.allussdsessionslocal.AllSessionsEntity
import com.monitoringtendepay.domain.models.AllUssdSessions

data class UssdData(
//    val Id: String,
    val msisdn: String,
    val sessionId: String,
    val sessionsDate: String,
    val timestamp: Long
)

fun UssdData.toEntity(timestamp: Long): AllSessionsEntity {
    return AllSessionsEntity(
        //  Id,
        msisdn,
        sessionId,
        sessionsDate,
        timestamp
    )
}

fun UssdData.toAllUssdSessions(): AllUssdSessions {
    return AllUssdSessions(
        msisdn,
        sessionId,
        sessionsDate
    )
}