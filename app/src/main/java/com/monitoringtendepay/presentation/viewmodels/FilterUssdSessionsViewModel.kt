package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetFilteredUssdSessionsUseCase
import com.monitoringtendepay.presentation.states.FilteredUssdSessionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class FilterUssdSessionsViewModel @Inject constructor(
    private val getFilteredUssdSessionsUseCase: GetFilteredUssdSessionsUseCase
) : ViewModel() {

    private val _filteredUssdSessionsState = Channel<FilteredUssdSessionsState>()
    val ussdSessionState = _filteredUssdSessionsState.receiveAsFlow()

    fun filterUssdSessions(action: String, phoneNumber: String, sessionId: String, startDate: String, endDate: String) {
        getFilteredUssdSessionsUseCase(action, phoneNumber, sessionId, startDate, endDate).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _filteredUssdSessionsState.send(
                        FilteredUssdSessionsState(filterUssdSessions = result.data ?: emptyList())
                    )
                }
                is Resource.Loading -> {
                    _filteredUssdSessionsState.send(FilteredUssdSessionsState(isLoading = true))
                }
                is Resource.Error -> {
                    _filteredUssdSessionsState.send(
                        FilteredUssdSessionsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}