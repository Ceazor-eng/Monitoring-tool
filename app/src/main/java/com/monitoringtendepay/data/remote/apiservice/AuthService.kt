package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.login.LoginResponse
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterRequest
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("Tende_monitoring_tool-main/login_users_operations_monitoring_tool.php")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}