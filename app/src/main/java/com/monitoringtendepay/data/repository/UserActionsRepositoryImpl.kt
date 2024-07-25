package com.monitoringtendepay.data.repository

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.useractions.MakeAdminRequest
import com.monitoringtendepay.domain.models.Admin
import com.monitoringtendepay.domain.repository.UserActionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserActionsRepositoryImpl @Inject constructor(
    private val api: AuthService
) : UserActionsRepository {
    override suspend fun makeUserAdmin(action: String, username: String): Flow<Resource<Result<Admin>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.makeUserAdmin(MakeAdminRequest(action, username))
            if (response.isSuccessful) {
                response.body()?.let { makeUserAdminResponse ->
                    if (makeUserAdminResponse.data.status == "success") {
                        val newAdmin = Admin(
                            message = makeUserAdminResponse.data.message,
                            status = makeUserAdminResponse.data.status
                        )
                        emit(Resource.Success(Result.success(newAdmin)))
                    } else {
                        emit(Resource.Error(makeUserAdminResponse.data.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("UserActionsRepository", "Make user admin failed: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: IOException) {
            Log.d("UserActionsRepository", "Network error: ${e.localizedMessage}")
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Log.d("UserActionsRepository", "HTTP error: ${e.localizedMessage}")
            emit(Resource.Error("HTTP error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Log.d("UserActionsRepository", "Unexpected error: ${e.localizedMessage}")
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }
}