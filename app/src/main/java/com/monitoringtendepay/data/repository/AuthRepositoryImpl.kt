package com.monitoringtendepay.data.repository

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterRequest
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.models.RegisterUser
import com.monitoringtendepay.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun login(action: String, username: String, password: String): Flow<Resource<Result<LoginUser>>> = flow {
        emit(Resource.Loading())
        try {
            val response = authService.login(LoginRequest(action, username, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val user = LoginUser(
                        message = loginResponse.message,
                        status = loginResponse.status,
                        role = loginResponse.role,
                        username = loginResponse.username,
                        sessionToken = loginResponse.sessionToken,
                        salutation = loginResponse.salutation
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

    override suspend fun registerUser(action: String, email: String, firstName: String, lastName: String, phoneNumber: String, roleID: String, username: String): Flow<Resource<Result<RegisterUser>>> = flow {
        emit(Resource.Loading())
        try {
            val response = authService.register(RegisterRequest(action, email, firstName, lastName, phoneNumber, roleID, username))
            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    val user = RegisterUser(
                        message = registerResponse.message,
                        status = registerResponse.status,
                        username = registerResponse.username,
                        salutation = registerResponse.salutation,
                        otp = registerResponse.otp
                    )
                    emit(Resource.Success(Result.success(user)))
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("AuthRepository", "Registering failed: $errorMessage")
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