package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.CompleteMonthlyTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.toTotalMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.monthlytransactions.TotalMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toEntity
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompleteMonthlyTransactionsImpl @Inject constructor(
    private val api: AllPaymentsApi,
    private val dao: CompleteMonthlyTransactionsDao
) : CompleteMonthlyTransactionsRepository {
    override suspend fun getCompleteTransactions(action: String): TotalMonthlyTransactionsDto = withContext(Dispatchers.IO) {
        val validTime = System.currentTimeMillis() - 5000
        // check Room database first
        val cachedCompleteTransactions = dao.getAllCompleteTransactions(validTime)
        if (cachedCompleteTransactions != null) {
            return@withContext cachedCompleteTransactions.toTotalMonthlyTransactionsDto()
        }

        // fetch from api if not found in cache
        val completeTransactions = api.getTotalMonthlyTransactions(action)

        // save to Room Database
        dao.insertCompleteMonthlyTransactions(completeTransactions.toEntity(System.currentTimeMillis()))

        return@withContext completeTransactions
    }
}