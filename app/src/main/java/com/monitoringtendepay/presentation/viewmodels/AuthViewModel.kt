package com.monitoringtendepay.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.repository.AuthRepository
import com.monitoringtendepay.presentation.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _loginState = Channel<AuthState>()
    val loginState = _loginState.receiveAsFlow()

    private val _registerState = Channel<AuthState>()
    val registerState = _registerState.receiveAsFlow()

    fun login(action: String, username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(action, username, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "result: ${result.data}")
                        _loginState.send(AuthState(data = result.data.toString()))
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Logging in...")
                        _loginState.send(AuthState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Login error: ${result.message}")
                        _loginState.send(AuthState(error = result.message))
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
                        _registerState.send(AuthState(data = result.data.toString()))
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Registering user...")
                        _registerState.send(AuthState(isLoading = true))
                    }
                    is Resource.Error -> {
                        Log.d(TAG, "Login error: ${result.message}")
                        _loginState.send(AuthState(error = result.message))
                    }
                }
            }
        }
    }
}