package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetCompleteMonthlyTransactionsUseCase
import com.monitoringtendepay.presentation.states.AllPaymentsState
import com.monitoringtendepay.presentation.states.CompleteTransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class AllPaymentsViewModel @Inject constructor(
    private val getAllPaymentsUseCase: GetAllPaymentsUseCase,
    private val getCompleteMonthlyTransactionsUseCase: GetCompleteMonthlyTransactionsUseCase
) : ViewModel() {

    private val _allPaymentsState = Channel<AllPaymentsState>()
    val paymentState = _allPaymentsState.receiveAsFlow()

    private val _completeTransactionsState = Channel<CompleteTransactionsState>()
    val completeTransactionsState = _completeTransactionsState.receiveAsFlow()

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

    fun fetchCompleteMonthlyTransactions(action: String) {
        getCompleteMonthlyTransactionsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _completeTransactionsState.send(
                        CompleteTransactionsState(completeTransactions = result.data)
                    )
                }
                is Resource.Loading -> {
                    _completeTransactionsState.send(CompleteTransactionsState(isLoading = true))
                }
                is Resource.Error -> {
                    _completeTransactionsState.send(
                        CompleteTransactionsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}