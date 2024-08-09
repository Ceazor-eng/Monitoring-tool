package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Constants.CACHE_TIME
import com.monitoringtendepay.data.localdatasource.allussdsessionslocal.UssdSessionsDao
import com.monitoringtendepay.data.localdatasource.allussdsessionslocal.toAllSessionsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.ussd.UssdData
import com.monitoringtendepay.data.remote.dto.ussd.toEntity
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllUssdSessionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: UssdSessionsDao
) : UssdSessionsRepository {
    override suspend fun getUssdSessions(action: String): List<UssdData> = withContext(Dispatchers.IO) {
        val validTime = System.currentTimeMillis() - CACHE_TIME

        // check Room database first
        val cachedUssdSessions = dao.getAllUssdSessions(validTime)
        if (cachedUssdSessions.isNotEmpty()) {
            return@withContext cachedUssdSessions.map { it.toAllSessionsDto() }
        }

        // fetch from api if not found in cache
        val ussdSessions = api.getAllUssdSessions(action)

        // save to Room Database
        dao.insertSessions(ussdSessions.data.map { it.toEntity(System.currentTimeMillis()) })

        return@withContext ussdSessions.data
    }
}