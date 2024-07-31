package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.models.RegisterUser
import com.monitoringtendepay.domain.models.UpdatePassword
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(action: String, username: String, password: String): Flow<Resource<Result<LoginUser>>>

    suspend fun registerUser(
        action: String,
        email: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        roleID: String,
        username: String
    ): Flow<Resource<RegisterUser>>

    suspend fun updatePassword(action: String, username: String, password: String): Flow<Resource<Result<UpdatePassword>>>
}