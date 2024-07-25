package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.allusers.UsersResponse
import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.login.LoginResponse
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterRequest
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterResponse
import com.monitoringtendepay.data.remote.dto.updatepassword.UpdatePasswordRequest
import com.monitoringtendepay.data.remote.dto.updatepassword.UpdatePasswordResponse
import com.monitoringtendepay.data.remote.dto.useractions.MakeAdminRequest
import com.monitoringtendepay.data.remote.dto.useractions.MakeAdminResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("Tende_monitoring_tool-main/login_users_operations_monitoring_tool.php")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun getAllUsers(@Query("action") action: String): UsersResponse

    @POST("Tende_monitoring_tool-main/login_users_operations_monitoring_tool.php")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): Response<UpdatePasswordResponse>

    @POST("Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun makeUserAdmin(@Body makeAdminRequest: MakeAdminRequest): Response<MakeAdminResponse>
}