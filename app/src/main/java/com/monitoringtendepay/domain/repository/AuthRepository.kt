package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.data.remote.dto.login.LoginResponseDto

interface AuthRepository {
    suspend fun loginUser(username: String, password: String, action: String): List<LoginResponseDto>
}