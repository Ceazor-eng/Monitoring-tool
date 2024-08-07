package com.monitoringtendepay.domain.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.dto.allusers.UsersResponse
import kotlinx.coroutines.flow.Flow

interface AllUsersRepository {
    suspend fun allUsers(action: String): Flow<Resource<UsersResponse>>
}