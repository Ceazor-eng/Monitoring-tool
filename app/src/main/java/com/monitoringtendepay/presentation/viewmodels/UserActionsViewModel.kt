package com.monitoringtendepay.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.repository.UserActionsRepository
import com.monitoringtendepay.presentation.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserActionsViewModel @Inject constructor(
    private val userActionsRepository: UserActionsRepository
) : ViewModel() {

    private val TAG = "UserActionsViewModel"

    private val _makeAdminState = Channel<AuthState>()
    val makeAdminState = _makeAdminState.receiveAsFlow()

    fun makeUserAdmin(action: String, username: String) {
        viewModelScope.launch {
            userActionsRepository.makeUserAdmin(action, username).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Make user admin successful: ${result.data}")
                        val newAdmin = result.data?.getOrNull()
                        if (newAdmin != null && newAdmin.status == "success") {
                            _makeAdminState.send(AuthState(data = newAdmin.toString()))
                        } else {
                            _makeAdminState.send(AuthState(error = "Make user admin failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Making user admin...")
                        _makeAdminState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Make user admin error: ${result.message}")
                        _makeAdminState.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }
}