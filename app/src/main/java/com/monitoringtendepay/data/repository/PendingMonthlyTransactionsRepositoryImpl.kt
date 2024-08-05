package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Constants.CACHE_TIME
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.PendingTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.toPendingMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.PendingMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toEntity
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PendingMonthlyTransactionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: PendingTransactionsDao
) : PendingMonthlyTransactionsRepository {
    override suspend fun getPendingTransactions(action: String): PendingMonthlyTransactionsDto = withContext(Dispatchers.IO) {
        val validTime = System.currentTimeMillis() - CACHE_TIME

        // check the Room database first
        val cachedPendingTransactions = dao.getAllPendingTransactions(validTime)
        if (cachedPendingTransactions != null) {
            return@withContext cachedPendingTransactions.toPendingMonthlyTransactionsDto()
        }
        // fetch from api if not found in cache
        val pendingTransactions = api.getPendingMonthlyTransactions(action)

        // save to Room Database
        dao.insertPendingTransactions(pendingTransactions.toEntity(System.currentTimeMillis()))

        return@withContext pendingTransactions
    }
}