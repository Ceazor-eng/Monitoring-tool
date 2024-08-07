package com.monitoringtendepay.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.PreferenceManager
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.repository.AuthRepository
import com.monitoringtendepay.presentation.states.AuthState
import com.monitoringtendepay.presentation.states.LoginState
import com.monitoringtendepay.presentation.states.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _loginState = Channel<LoginState>()
    val loginState = _loginState.receiveAsFlow()

    private val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()

    private val _updatePasswordState = Channel<AuthState>()
    val updatePasswordState = _updatePasswordState.receiveAsFlow()

    fun login(action: String, username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(action, username, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val loginResponse = result.data?.getOrNull()
                        if (loginResponse != null) {
                            Log.d(TAG, "LoginResponse received: $loginResponse")

                            // Check the status to set changePasswordRequired flag
                            val changePasswordRequired = loginResponse.status == "changePassword"
                            Log.d(TAG, "Login status: ${loginResponse.status}")
                            Log.d(TAG, "Change password required: $changePasswordRequired")

                            _loginState.send(
                                LoginState(
                                    data = loginResponse,
                                    changePasswordRequired = changePasswordRequired
                                )
                            )
                            // Save login details in preferences
                            preferenceManager.setLoggedIn(true)
                            preferenceManager.setSessionToken(loginResponse.sessionToken)
                            preferenceManager.setUsername(loginResponse.username)
                            preferenceManager.setRole(loginResponse.role)
                        } else {
                            Log.d(TAG, "LoginResponse is null")
                            _loginState.send(LoginState(error = "Login failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Logging in...")
                        _loginState.send(LoginState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Login error: ${result.message}")
                        _loginState.send(LoginState(error = result.message))
                    }
                }
            }
        }
    }

    fun registerUser(action: String, email: String, firstName: String, lastName: String, phoneNumber: String, roleID: String, username: String) {
        viewModelScope.launch {
            authRepository.registerUser(action, email, firstName, lastName, phoneNumber, roleID, username).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "result: ${result.data}")
                        _registerState.send(RegisterState(data = result.data))
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Registering user...")
                        _registerState.send(RegisterState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Login error: ${result.message}")
                        _registerState.send(RegisterState(error = result.message))
                    }
                }
            }
        }
    }

    fun updatePassword(action: String, username: String, password: String) {
        viewModelScope.launch {
            authRepository.updatePassword(action, username, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Update password successful: ${result.data}")
                        val newPassword = result.data?.getOrNull()
                        if (newPassword != null && newPassword.status == "success") {
                            _updatePasswordState.send(AuthState(data = newPassword.toString()))
                        } else {
                            _updatePasswordState.send(AuthState(error = "Update password failed"))
                        }
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Updating password...")
                        _updatePasswordState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Update password error: ${result.message}")
                        _updatePasswordState.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }
}