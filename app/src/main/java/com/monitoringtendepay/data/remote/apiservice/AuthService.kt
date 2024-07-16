package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}