package com.monitoringtendepay.data.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.dto.login.LoginRequestDto
import com.monitoringtendepay.data.remote.dto.login.LoginResponseDto
import com.monitoringtendepay.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AllPaymentsApi
) : AuthRepository {
    override suspend fun loginUser(username: String, password: String, action: String): List<LoginResponseDto> {
        val response = api.loginUser(LoginRequestDto(username, password), action)
        return response.data
    }
}