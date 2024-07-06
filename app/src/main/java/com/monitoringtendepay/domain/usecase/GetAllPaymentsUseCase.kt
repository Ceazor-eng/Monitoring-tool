package com.monitoringtendepay.domain.usecase

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.toAllPayments
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
            val payments = repository.getAllPayments(action).map { it.toAllPayments() }
            emit(Resource.Success(payments))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<AllPayments>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error<List<AllPayments>>(
                    e.localizedMessage ?: "Could not reach the server.Check your internet connection"
                )
            )
        }
    }
}