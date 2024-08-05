package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Constants.CACHE_TIME
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.FailedTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.toFailedTransactionsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.FailedMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toEntity
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FailedTransactionsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: FailedTransactionsDao
) : FailedTransactionsRepository {
    override suspend fun getFailedTransactions(action: String): FailedMonthlyTransactionsDto = withContext(Dispatchers.IO) {
        val validTime = System.currentTimeMillis() - CACHE_TIME
        // check the Room database first
        val cachedFailedTransactions = dao.getAllFailedTransactions(validTime)
        if (cachedFailedTransactions != null) {
            return@withContext cachedFailedTransactions.toFailedTransactionsDto()
        }
        // fetch from api if not found in cache
        val failedTransactions = api.getFailedMonthlyTransactions(action)

        // save to Room Database
        dao.insertFailedTransactions(failedTransactions.toEntity(System.currentTimeMillis()))

        return@withContext failedTransactions
    }
}