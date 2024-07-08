package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetAllUssdSessionsUseCase
import com.monitoringtendepay.presentation.states.AllUssdSessionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AllUssdSessionsViewModel @Inject constructor(
    private val getAllUssdSessionsUseCase:GetAllUssdSessionsUseCase
) :ViewModel(){

    private val _allUssdSessionsState = Channel<AllUssdSessionsState>()
    val ussdSessionsState = _allUssdSessionsState.receiveAsFlow()

    fun fetchAllUssdSessions(action: String) {
        getAllUssdSessionsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _allUssdSessionsState.send(
                        AllUssdSessionsState(ussdSessions = result.data ?: emptyList())
                    )
                }
                is Resource.Loading -> {
                    _allUssdSessionsState.send(AllUssdSessionsState(isLoading = true))
                }
                is Resource.Error -> {
                    _allUssdSessionsState.send(
                        AllUssdSessionsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}