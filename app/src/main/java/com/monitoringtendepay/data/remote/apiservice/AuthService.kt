package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.allusers.UsersResponse
import com.monitoringtendepay.data.remote.dto.login.LoginRequest
import com.monitoringtendepay.data.remote.dto.login.LoginResponse
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterRequest
import com.monitoringtendepay.data.remote.dto.registerusers.RegisterResponse
import com.monitoringtendepay.data.remote.dto.updatepassword.UpdatePasswordRequest
import com.monitoringtendepay.data.remote.dto.updatepassword.UpdatePasswordResponse
import com.monitoringtendepay.data.remote.dto.useractions.activate.ActivateUserRequest
import com.monitoringtendepay.data.remote.dto.useractions.activate.ActivateUserResponse
import com.monitoringtendepay.data.remote.dto.useractions.deactivate.DeactivateUserRequest
import com.monitoringtendepay.data.remote.dto.useractions.deactivate.DeactivateUserResponse
import com.monitoringtendepay.data.remote.dto.useractions.makeadmin.MakeAdminRequest
import com.monitoringtendepay.data.remote.dto.useractions.makeadmin.MakeAdminResponse
import com.monitoringtendepay.data.remote.dto.useractions.resendotp.ResendOtpRequest
import com.monitoringtendepay.data.remote.dto.useractions.resendotp.ResendOtpResponse
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

    @POST("Tende_monitoring_tool-main/login_users_operations_monitoring_tool.php")
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): Response<ResendOtpResponse>

    @POST("/Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun activateUser(@Body activateUserRequest: ActivateUserRequest): Response<ActivateUserResponse>

    @POST("/Tende_monitoring_tool-main/user_operations_monitoring_tool.php")
    suspend fun deactivateUser(@Body deactivateUserRequest: DeactivateUserRequest): Response<DeactivateUserResponse>
}