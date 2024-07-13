package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.filterussdsessions.toFilteredUssdSessions
import com.monitoringtendepay.domain.models.FilterUssdSessionsParams
import com.monitoringtendepay.domain.repository.FilterUssdSessionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetFilteredUssdSessionsUseCase @Inject constructor(
    private val repository: FilterUssdSessionsRepository
) {
    operator fun invoke(action: String, phoneNumber: String, sessionId: String, startDate: String, endDate: String): Flow<Resource<List<FilterUssdSessionsParams>>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetFilteredUssdSessionsUseCase", "Fetching filtered ussd sessions...")
            val filteredUssdSessionsDto = repository.filterUssdSessions(action, phoneNumber, sessionId, startDate, endDate)
            Log.d("GetFilteredUssdSessionsUseCase", "Fetched sessions DTO: $filteredUssdSessionsDto")

            // Null check before mapping
            val filteredUssdSessions = filteredUssdSessionsDto?.let {
                if (it.isNotEmpty()) {
                    it.map { dto -> dto.toFilteredUssdSessions() }
                } else {
                    emptyList()
                }
            } ?: emptyList()

            Log.d("GetFilteredUssdSessionsUseCase", "Mapped ussd sessions: $filteredUssdSessions")
            emit(Resource.Success(filteredUssdSessions))
        } catch (e: HttpException) {
            Log.e("GetFilteredUssdSessionsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<FilterUssdSessionsParams>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetFilteredPaymentsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<FilterUssdSessionsParams>>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}