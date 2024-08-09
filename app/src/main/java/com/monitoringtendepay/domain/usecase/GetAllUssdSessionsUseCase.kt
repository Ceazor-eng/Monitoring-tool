package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.ussd.toAllUssdSessions
import com.monitoringtendepay.domain.models.AllUssdSessions
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetAllUssdSessionsUseCase @Inject constructor(
    private val repository: UssdSessionsRepository
) {

    operator fun invoke(action: String): Flow<Resource<List<AllUssdSessions>>> = flow {
        try {
            emit(Resource.Loading<List<AllUssdSessions>>())
            Log.d("GetAllUssdSessionsUseCase", "Fetching sessions...")
            val fetchUssdSessions = repository.getUssdSessions(action)
            Log.d("GetAllUssdSessionsUseCase", "Fetched ussdSessions DTO: $fetchUssdSessions")

            if (fetchUssdSessions.isNullOrEmpty()) {
                Log.e("GetAllUssdSessionsUseCase", "ussdSessionsDto is null or empty")
                emit(Resource.Error("No data found"))
            } else {
                val ussdSessions = fetchUssdSessions.map { it.toAllUssdSessions() }
                Log.d("GetAllUssdSessionsUseCase", "Mapped ussdSessions: $ussdSessions")
                emit(Resource.Success(ussdSessions))
            }
        } catch (e: HttpException) {
            Log.e("GetAllUssdSessionsUseCase", "HttpException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<AllUssdSessions>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            Log.e("GetAllUssdSessionsUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<AllUssdSessions>>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        }
    }
}