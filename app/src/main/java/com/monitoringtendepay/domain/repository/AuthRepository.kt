package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.LoginUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<Result<LoginUser>>>
}