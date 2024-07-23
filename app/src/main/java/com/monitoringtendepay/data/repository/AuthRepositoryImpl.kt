package com.monitoringtendepay.data.repository

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterRequest
import com.monitoringtendepay.data.remote.dto.updatepassword.UpdatePasswordRequest
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.models.RegisterUser
import com.monitoringtendepay.domain.models.UpdatePassword
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
            Log.d("AuthRepository", "API Response: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    if (loginResponse.status == "success" || loginResponse.status == "changePassword") {
                        val user = LoginUser(
                            message = loginResponse.message,
                            status = loginResponse.status,
                            role = loginResponse.role,
                            username = loginResponse.username,
                            sessionToken = loginResponse.sessionToken,
                            salutation = loginResponse.salutation
                        )
                        emit(Resource.Success(Result.success(user)))
                    } else {
                        emit(Resource.Error(loginResponse.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorMessage = response.message() ?: "Unknown error"
                val errorBody = response.errorBody()?.string() ?: "No error body"
                Log.d("AuthRepository", "Login failed: $errorMessage. Error body: $errorBody")
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
                    if (registerResponse.data.status == "success") {
                        val user = RegisterUser(
                            message = registerResponse.data.message,
                            status = registerResponse.data.status,
                            username = registerResponse.data.username,
                            salutation = registerResponse.data.salutation,
                            otp = registerResponse.data.otp
                        )
                        emit(Resource.Success(Result.success(user)))
                    } else {
                        emit(Resource.Error(registerResponse.data.message ?: "Unexpected error"))
                    }
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

    override suspend fun updatePassword(action: String, username: String, password: String): Flow<Resource<Result<UpdatePassword>>> = flow {
        emit(Resource.Loading())

        try {
            val response = authService.updatePassword(UpdatePasswordRequest(action, username, password))
            if (response.isSuccessful) {
                response.body()?.let { updatePasswordResponse ->
                    if (updatePasswordResponse.status == "success") {
                        val newPassword = UpdatePassword(
                            message = updatePasswordResponse.message,
                            status = updatePasswordResponse.status
                        )
                        emit(Resource.Success(Result.success(newPassword)))
                    } else {
                        emit(Resource.Error(updatePasswordResponse.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("AuthRepository", "Update Password failed: $errorMessage")
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