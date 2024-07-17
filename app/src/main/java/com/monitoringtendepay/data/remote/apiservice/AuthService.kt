package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("Tende_monitoring_tool-main/login_users_operations_monitoring_tool.php")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}