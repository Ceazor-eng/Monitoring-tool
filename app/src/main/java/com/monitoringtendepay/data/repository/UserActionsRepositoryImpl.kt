package com.monitoringtendepay.data.repository

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.useractions.activate.ActivateUserRequest
import com.monitoringtendepay.data.remote.dto.useractions.deactivate.DeactivateUserRequest
import com.monitoringtendepay.data.remote.dto.useractions.makeadmin.MakeAdminRequest
import com.monitoringtendepay.data.remote.dto.useractions.resendotp.ResendOtpRequest
import com.monitoringtendepay.domain.models.ActivateUser
import com.monitoringtendepay.domain.models.Admin
import com.monitoringtendepay.domain.models.DeactivateUser
import com.monitoringtendepay.domain.models.ResendOtp
import com.monitoringtendepay.domain.repository.UserActionsRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
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
                val errorBody = response.errorBody()?.string() ?: "No error body"
                val errorMessage = try {
                    JSONObject(errorBody).getString("error")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Log.d("UserActionsRepository", "Make User Admin failed. Error body: $errorBody")
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

    override suspend fun resendOtp(action: String, username: String): Flow<Resource<Result<ResendOtp>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.resendOtp(ResendOtpRequest(action, username))
            if (response.isSuccessful) {
                response.body()?.let { resendOtpResponse ->
                    if (resendOtpResponse.data.status == "success") {
                        val newOtp = ResendOtp(
                            message = resendOtpResponse.data.message,
                            newOtp = resendOtpResponse.data.newOtp,
                            status = resendOtpResponse.data.status,
                            username = resendOtpResponse.data.username,
                            phoneNumber = resendOtpResponse.data.phoneNumber
                        )
                        emit(Resource.Success(Result.success(newOtp)))
                    } else {
                        emit(Resource.Error(resendOtpResponse.data.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                val errorMessage = try {
                    JSONObject(errorBody).getString("error")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Log.d("UserActionsRepository", "Resend OTP failed. Error body: $errorBody")
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

    override suspend fun activateUser(action: String, username: String): Flow<Resource<Result<ActivateUser>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.activateUser(ActivateUserRequest(action, username))
            if (response.isSuccessful) {
                response.body()?.let { activateUserResponse ->
                    if (activateUserResponse.data.status == "success") {
                        val activatedUser = ActivateUser(
                            message = activateUserResponse.data.message,
                            status = activateUserResponse.data.status
                        )
                        emit(Resource.Success(Result.success(activatedUser)))
                    } else {
                        emit(Resource.Error(activateUserResponse.data.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                val errorMessage = try {
                    JSONObject(errorBody).getString("error")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Log.d("UserActionsRepository", "Activate User failed. Error body: $errorBody")
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

    override suspend fun deactivateUser(action: String, username: String): Flow<Resource<Result<DeactivateUser>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.deactivateUser(DeactivateUserRequest(action, username))
            if (response.isSuccessful) {
                response.body()?.let { deactivateUserResponse ->
                    if (deactivateUserResponse.data.status == "success") {
                        val deactivatedUser = DeactivateUser(
                            message = deactivateUserResponse.data.message,
                            status = deactivateUserResponse.data.status
                        )
                        emit(Resource.Success(Result.success(deactivatedUser)))
                    } else {
                        emit(Resource.Error(deactivateUserResponse.data.message ?: "Unexpected error"))
                    }
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                val errorMessage = try {
                    JSONObject(errorBody).getString("error")
                } catch (e: Exception) {
                    "Unknown error"
                }
                Log.d("UserActionsRepository", "Deactivate user failed. Error body: $errorBody")
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