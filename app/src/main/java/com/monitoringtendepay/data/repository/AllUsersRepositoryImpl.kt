package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.remote.dto.allusers.UsersResponse
import com.monitoringtendepay.domain.repository.AllUsersRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AllUsersRepositoryImpl @Inject constructor(
    private val api: AuthService
) : AllUsersRepository {
    override suspend fun allUsers(action: String): Flow<Resource<UsersResponse>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getAllUsers(action)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An Unknown error occurred"))
        }
    }
}