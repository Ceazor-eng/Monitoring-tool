package com.monitoringtendepay.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.repository.AllUsersRepository
import com.monitoringtendepay.presentation.states.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AllUsersViewModel @Inject constructor(
    private val repository: AllUsersRepository
) : ViewModel() {

    private val _allUsersState = MutableStateFlow(UsersState())
    val usersState: StateFlow<UsersState> = _allUsersState

    fun fetchAllUsers(action: String) {
        viewModelScope.launch {
            repository.allUsers(action).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d("AllUsersViewModel", "Fetching users successful: ${result.data}")
                        val users = result.data?.data?.users?.map { it.userDetails } ?: emptyList()
                        _allUsersState.value = UsersState(users = users)
                    }
                    is Resource.Loading -> {
                        Log.d("AllUsersViewModel", "Loading ...")
                        _allUsersState.value = UsersState(isLoading = true)
                    }
                    is Resource.Error -> {
                        Log.d("AllUsersViewModel", "Error fetching users: ${result.message}")
                        _allUsersState.value = UsersState(error = result.message ?: "Unknown error occurred")
                    }
                }
            }
        }
    }
}