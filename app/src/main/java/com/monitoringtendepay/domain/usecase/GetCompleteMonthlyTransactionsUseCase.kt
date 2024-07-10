package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toCompleteTransactions
import com.monitoringtendepay.domain.models.CompleteTransactions
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetCompleteMonthlyTransactionsUseCase @Inject constructor(
    private val repository: CompleteMonthlyTransactionsRepository
) {
    operator fun invoke(action: String): Flow<Resource<CompleteTransactions>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetCompleteMonthlyTransactionsUseCase", "Fetching complete monthly transactions...")
            val completeTransactionsDto = repository.getCompleteTransactions(action)
            Log.d("GetCompleteMonthlyTransactionsUseCase", "Fetched complete monthly transactions DTO: $completeTransactionsDto")
            val completeTransactions = completeTransactionsDto.toCompleteTransactions()
            Log.d("GetCompleteMonthlyTransactionsUseCase", "Mapped complete transactions: $completeTransactions")
            emit(Resource.Success(completeTransactions))
        } catch (e: HttpException) {
            Log.e("GetCompleteMonthlyTransactionsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<CompleteTransactions>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetCompleteMonthlyTransactionsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<CompleteTransactions>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}