package com.monitoringtendepay.domain.usecase

import android.util.Log
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.login.toLoginUser
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(username: String, password: String, action: String): Flow<Resource<List<LoginUser>>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("GetLoginUseCase", "Fetching login response for username: $username, action: $action")
            val loginResponseDto = repository.loginUser(username, password, action)
            Log.d("GetLoginUseCase", "Fetched login response DTO: $loginResponseDto")
            // Handle null or empty response
            if (loginResponseDto == null || loginResponseDto.isEmpty()) {
                emit(Resource.Error<List<LoginUser>>("Login failed: Invalid credentials or server error"))
                return@flow
            }
            val loginUsers = loginResponseDto.map { it.toLoginUser() }
            Log.d("GetLoginUseCase", "Mapped login users: $loginUsers")
            emit(Resource.Success(loginUsers))
        } catch (e: IOException) {
            Log.e("GetLoginUseCase", "IOException: ${e.localizedMessage}")
            emit(
                Resource.Error<List<LoginUser>>(
                    e.localizedMessage ?: "Could not reach the server. Check your internet connection"
                )
            )
        } catch (e: Exception) {
            Log.e("GetLoginUseCase", "Exception: ${e.localizedMessage}")
            emit(Resource.Error<List<LoginUser>>(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}