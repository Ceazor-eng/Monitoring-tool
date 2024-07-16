package com.monitoringtendepay.data.repository

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<Result<LoginUser>>> = flow {
        emit(Resource.Loading())
        try {
            val response = authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val user = LoginUser(
                        id = "", // Set appropriately if there's an id in response
                        email = email,
                        name = loginResponse.name, // Set appropriately if there's a name in response
                        token = loginResponse.token
                    )
                    emit(Resource.Success(Result.success(user)))
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("AuthRepository", "Login failed: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: IOException) {
            Log.d("AuthRepository", "Network error: ${e.localizedMessage}")
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Log.d("AuthRepository", "HTTP error: ${e.localizedMessage}")
            emit(Resource.Error("HTTP error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Log.d("AuthRepository", "Unexpected error: ${e.localizedMessage}")
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }
}