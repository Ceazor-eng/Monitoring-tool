package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Constants.CACHE_TIME
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.MissingTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.toMissingMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.MissingPaymentsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toEntity
import com.monitoringtendepay.domain.repository.MissingPaymentsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MissingPaymentsRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: MissingTransactionsDao
) : MissingPaymentsRepository {
    override suspend fun getMissingTransactions(action: String): MissingPaymentsDto = withContext(Dispatchers.IO) {
        val validTime = System.currentTimeMillis() - CACHE_TIME

        // check the Room database first
        val cachedMissingTransactions = dao.getAllMissingTransactions(validTime)
        if (cachedMissingTransactions != null) {
            return@withContext cachedMissingTransactions.toMissingMonthlyTransactionsDto()
        }
        // fetch from api if not found in cache
        val missingTransactions = api.getMissingPayments(action)

        // save to Room Database
        dao.insertMissingTransactions(missingTransactions.toEntity(System.currentTimeMillis()))

        return@withContext missingTransactions
    }
}