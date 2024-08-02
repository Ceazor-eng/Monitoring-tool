package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.localdatasource.AllPaymentsDao
import com.monitoringtendepay.data.localdatasource.toFetchAllPaymentsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.payments.FetchAllPaymentsDto
import com.monitoringtendepay.data.remote.dto.payments.toEntity
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllPaymentsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: AllPaymentsDao
) : AllPaymentsRepository {
    override suspend fun getAllPayments(action: String): List<FetchAllPaymentsDto> = withContext(Dispatchers.IO) {
        // check Room database first
        val cachedPayments = dao.getAllPayments()
        if (cachedPayments.isNotEmpty()) {
            return@withContext cachedPayments.map { it.toFetchAllPaymentsDto() }
        }

        // fetch from api if not found in cache
        val payments = api.getAllPayments(action)

        // save to Room Database
        dao.insertPayments(payments.data.map { it.toEntity() })

        return@withContext payments.data
    }
}