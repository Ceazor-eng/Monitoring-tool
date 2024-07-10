package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toFailedTransactions
import com.monitoringtendepay.domain.models.FailedTransactions
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetFailedTransactionsUseCase @Inject constructor(
    private val repository: FailedTransactionsRepository
) {

    operator fun invoke(action: String): Flow<Resource<FailedTransactions>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetFailedMonthlyTransactionsUseCase", "Fetching failed monthly transactions...")
            val failedTransactionsDto = repository.getFailedTransactions(action)
            Log.d("GetFailedMonthlyTransactionsUseCase", "Fetched failed monthly transactions DTO: $failedTransactionsDto")
            val failedTransactions = failedTransactionsDto.toFailedTransactions()
            Log.d("GetFailedMonthlyTransactionsUseCase", "Mapped failed transactions: $failedTransactions")
            emit(Resource.Success(failedTransactions))
        } catch (e: HttpException) {
            Log.e("GetFailedMonthlyTransactionsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<FailedTransactions>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetFailedMonthlyTransactionsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<FailedTransactions>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}