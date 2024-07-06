package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
import com.monitoringtendepay.presentation.states.AllPaymentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class AllPaymentsViewModel @Inject constructor(
    private val getAllPaymentsUseCase: GetAllPaymentsUseCase
) : ViewModel() {

    private val _allPaymentsState = Channel<AllPaymentsState>()
    val paymentState = _allPaymentsState.receiveAsFlow()

    fun fetchAllPayments(action: String) {
        getAllPaymentsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _allPaymentsState.send(
                        AllPaymentsState(payments = result.data ?: emptyList())
                    )
                }
                is Resource.Loading -> { _allPaymentsState.send(AllPaymentsState(isLoading = true)) }
                is Resource.Error -> {
                    _allPaymentsState.send(
                        AllPaymentsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}