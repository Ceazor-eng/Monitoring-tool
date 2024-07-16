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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Login successful")
                        _loginState.send(AuthState(data = "LogIn Successfully"))
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Logging in...")
                        _loginState.send(AuthState(isLoading = true))
                    }
                    is Resource.Error -> {
                        val errorMessage = when (result.message) {
                            "Incorrect password" -> "Incorrect password"
                            "Email not found" -> "Email not found"
                            else -> "Login Failed!!"
                        }
                        Log.d(TAG, "Login error: $errorMessage")
                        _loginState.send(AuthState(error = errorMessage))
                    }
                }
            }
        }
    }
}