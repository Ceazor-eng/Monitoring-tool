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

    private val _activateUser = Channel<AuthState>()
    val resendOtpState = _activateUser.receiveAsFlow()

    private val _activateUserState = Channel<AuthState>()
    val activateUserState = _activateUserState.receiveAsFlow()

    private val _deactivateUserState = Channel<AuthState>()
    val deactivateUserState = _deactivateUserState.receiveAsFlow()

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

    fun resendOtp(action: String, username: String) {
        viewModelScope.launch {
            userActionsRepository.resendOtp(action, username).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Resend Otp successful: ${result.data}")
                        val newOtp = result.data?.getOrNull()
                        if (newOtp != null && newOtp.status == "success") {
                            _activateUser.send(AuthState(data = newOtp.toString()))
                        } else {
                            _activateUser.send(AuthState(error = "Resend Otp failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Resending Otp...")
                        _activateUser.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Resend new otp error: ${result.message}")
                        _activateUser.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }

    fun activateUser(action: String, username: String) {
        viewModelScope.launch {
            userActionsRepository.activateUser(action, username).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Activate user successful: ${result.data}")
                        val activatedUser = result.data?.getOrNull()
                        if (activatedUser != null && activatedUser.status == "success") {
                            _activateUserState.send(AuthState(data = activatedUser.toString()))
                        } else {
                            _activateUserState.send(AuthState(error = "Activate user failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Activating user...")
                        _activateUserState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Activate user error: ${result.message}")
                        _activateUserState.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }

    fun deactivateUser(action: String, username: String) {
        viewModelScope.launch {
            userActionsRepository.deactivateUser(action, username).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Deactivate user successful: ${result.data}")
                        val deactivatedUser = result.data?.getOrNull()
                        if (deactivatedUser != null && deactivatedUser.status == "success") {
                            _deactivateUserState.send(AuthState(data = deactivatedUser.toString()))
                        } else {
                            _deactivateUserState.send(AuthState(error = "Deactivate user failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Deactivating user...")
                        _deactivateUserState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Deactivate user error: ${result.message}")
                        _deactivateUserState.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }
}