package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.monthlytransactions.toMissingPayments
import com.monitoringtendepay.domain.models.MissingPayments
import com.monitoringtendepay.domain.repository.MissingPaymentsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetMissingPaymentsUseCase @Inject constructor(
    private val repository: MissingPaymentsRepository
) {

    operator fun invoke(action: String): Flow<Resource<MissingPayments>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetMissingPaymentsUseCase", "Fetching missing monthly payments...")
            val missingPaymentsDto = repository.getMissingTransactions(action)
            Log.d("GetMissingPaymentsUseCase", "Fetched missing monthly payments DTO: $missingPaymentsDto")
            val missingPayments = missingPaymentsDto.toMissingPayments()
            Log.d("GetMissingPaymentsUseCase", "Mapped missing payments: $missingPayments")
            emit(Resource.Success(missingPayments))
        } catch (e: HttpException) {
            Log.e("GetMissingPaymentsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<MissingPayments>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetMissingPaymentsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<MissingPayments>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}