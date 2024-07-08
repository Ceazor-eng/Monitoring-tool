package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.ussd.UssdData

interface UssdSessionsRepository {
    suspend fun getUssdSessions(action:String):List<UssdData>

}