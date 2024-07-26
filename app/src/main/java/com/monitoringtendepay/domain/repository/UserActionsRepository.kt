package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.ActivateUser
import com.monitoringtendepay.domain.models.Admin
import com.monitoringtendepay.domain.models.DeactivateUser
import com.monitoringtendepay.domain.models.ResendOtp
import kotlinx.coroutines.flow.Flow

interface UserActionsRepository {

    suspend fun makeUserAdmin(action: String, username: String): Flow<Resource<Result<Admin>>>

    suspend fun resendOtp(action: String, username: String): Flow<Resource<Result<ResendOtp>>>

    suspend fun activateUser(action: String, username: String): Flow<Resource<Result<ActivateUser>>>

    suspend fun deactivateUser(action: String, username: String): Flow<Resource<Result<DeactivateUser>>>
}