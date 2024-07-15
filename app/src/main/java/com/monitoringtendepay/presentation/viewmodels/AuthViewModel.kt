package com.monitoringtendepay.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetLoginUseCase
import com.monitoringtendepay.presentation.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getLoginUseCase: GetLoginUseCase
) : ViewModel() {

    private val _loginState = Channel<LoginState>()
    val loginState: Flow<LoginState> = _loginState.receiveAsFlow()

    fun loginUser(username: String, password: String, action: String) {
        Log.d("AuthViewModel", "Initiating login with username: $username, action: $action")
        viewModelScope.launch {
            getLoginUseCase(username, password, action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d("AuthViewModel", "Login successful: ${result.data}")
                        _loginState.send(LoginState(success = result.data))
                    }
                    is Resource.Loading -> {
                        Log.d("AuthViewModel", "Loading...")
                        _loginState.send(LoginState(isLoading = true))
                    }
                    is Resource.Error -> {
                        Log.e("AuthViewModel", "Error: ${result.message}")
                        _loginState.send(LoginState(error = result.message ?: "An unexpected error occurred"))
                    }
                }
            }.catch { e ->
                Log.e("AuthViewModel", "Exception: ${e.localizedMessage}")
                _loginState.send(LoginState(error = e.localizedMessage ?: "An unexpected error occurred"))
            }.collect()
        }
    }
}