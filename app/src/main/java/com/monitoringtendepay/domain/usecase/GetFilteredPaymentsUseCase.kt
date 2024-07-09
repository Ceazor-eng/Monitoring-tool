package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.filterpayments.toFilteredPayments
import com.monitoringtendepay.domain.models.PaymentsFilterParams
import com.monitoringtendepay.domain.repository.FilterPaymentsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetFilteredPaymentsUseCase @Inject constructor(
    private val repository: FilterPaymentsRepository
) {
    operator fun invoke(action: String, params: PaymentsFilterParams): Flow<Resource<List<PaymentsFilterParams>>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetFilteredPaymentsUseCase", "Fetching filtered payments...")
            val filteredPaymentsDto = repository.filterPayments(action, params)
            Log.d("GetFilteredPaymentsUseCase", "Fetched payments DTO: $filteredPaymentsDto")
            val payments = filteredPaymentsDto.map { it.toFilteredPayments() }
            Log.d("GetFilteredPaymentsUseCase", "Mapped payments: $payments")
            emit(Resource.Success(payments))
        } catch (e: HttpException) {
            Log.e("GetFilteredPaymentsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<PaymentsFilterParams>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetFilteredPaymentsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<PaymentsFilterParams>>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}