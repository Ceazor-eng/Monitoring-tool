package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toPendingMonthlyTransactions
import com.monitoringtendepay.domain.models.PendingTransactions
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetPendingMonthlyTransactionsUseCase @Inject constructor(
    private val repository: PendingMonthlyTransactionsRepository
) {

    operator fun invoke(action: String): Flow<Resource<PendingTransactions>> = flow {
        try {
            emit(Resource.Loading<PendingTransactions>())
            Log.d("GetPendingMonthlyTransactionsUseCase", "Fetching pending monthly transactions...")
            val pendingTransactionsDto = repository.getPendingTransactions(action)
            Log.d("GetPendingMonthlyTransactionsUseCase", "Fetched pending monthly transactions DTO: $pendingTransactionsDto")
            val pendingTransactions = pendingTransactionsDto.toPendingMonthlyTransactions()
            Log.d("GetPendingMonthlyTransactionsUseCase", "Mapped pending transactions: $pendingTransactions")
            emit(Resource.Success(pendingTransactions))
        } catch (e: HttpException) {
            Log.e("GetPendingMonthlyTransactionsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<PendingTransactions>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetPendingMonthlyTransactionsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<PendingTransactions>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}