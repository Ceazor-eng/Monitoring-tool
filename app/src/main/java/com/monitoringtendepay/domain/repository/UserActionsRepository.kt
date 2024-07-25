package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.Admin
import kotlinx.coroutines.flow.Flow

interface UserActionsRepository {

    suspend fun makeUserAdmin(action: String, username: String): Flow<Resource<Result<Admin>>>
}