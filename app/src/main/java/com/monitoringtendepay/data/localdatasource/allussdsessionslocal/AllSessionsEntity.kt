package com.monitoringtendepay.data.localdatasource.allussdsessionslocal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.ussd.UssdData

@Entity(tableName = "all_ussd_sessions")
data class AllSessionsEntity(
//     val id: String,
    @PrimaryKey val msisdn: String,
    val sessionId: String,
    val sessionsDate: String,
    val timestamp: Long
)

fun AllSessionsEntity.toAllSessionsDto(): UssdData {
    return UssdData(
        // id,
        msisdn,
        sessionId,
        sessionsDate,
        timestamp
    )
}