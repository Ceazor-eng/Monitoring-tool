package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.ussd.UssdData
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import javax.inject.Inject

class AllUssdSessionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
):UssdSessionsRepository{
    override suspend fun getUssdSessions(action: String): List<UssdData> {
        val response = api.getAllUssdSessions(action)
        return response.data
    }
}