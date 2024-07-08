package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.payments.toAllPayments
import com.monitoringtendepay.domain.models.AllPayments
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetAllPaymentsUseCase @Inject constructor(
    private val repository: AllPaymentsRepository
) {
    operator fun invoke(action: String): Flow<Resource<List<AllPayments>>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetAllPaymentsUseCase", "Fetching payments...")
            val paymentsDto = repository.getAllPayments(action)
            Log.d("GetAllPaymentsUseCase", "Fetched payments DTO: $paymentsDto")
            val payments = paymentsDto.map { it.toAllPayments() }
            Log.d("GetAllPaymentsUseCase", "Mapped payments: $payments")
            emit(Resource.Success(payments))
        } catch (e: HttpException) {
            Log.e("GetAllPaymentsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<AllPayments>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetAllPaymentsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<AllPayments>>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}