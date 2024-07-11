package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetCompleteMonthlyTransactionsUseCase
import com.monitoringtendepay.domain.usecase.GetFailedTransactionsUseCase
import com.monitoringtendepay.domain.usecase.GetMissingPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetPendingMonthlyTransactionsUseCase
import com.monitoringtendepay.presentation.states.AllPaymentsState
import com.monitoringtendepay.presentation.states.CompleteTransactionsState
import com.monitoringtendepay.presentation.states.FailedTransactionState
import com.monitoringtendepay.presentation.states.MissingPaymentsState
import com.monitoringtendepay.presentation.states.PendingTransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class AllPaymentsViewModel @Inject constructor(
    private val getAllPaymentsUseCase: GetAllPaymentsUseCase,
    private val getCompleteMonthlyTransactionsUseCase: GetCompleteMonthlyTransactionsUseCase,
    private val getPendingMonthlyTransactionsUseCase: GetPendingMonthlyTransactionsUseCase,
    private val getFailedTransactionsUseCase: GetFailedTransactionsUseCase,
    private val getMissingPaymentsUseCase: GetMissingPaymentsUseCase
) : ViewModel() {

    private val _allPaymentsState = Channel<AllPaymentsState>()
    val paymentState = _allPaymentsState.receiveAsFlow()

    private val _completeTransactionsState = Channel<CompleteTransactionsState>()
    val completeTransactionsState = _completeTransactionsState.receiveAsFlow()

    private val _pendingTransactionsState = Channel<PendingTransactionsState>()
    val pendingTransactionsState = _pendingTransactionsState.receiveAsFlow()

    private val _failedTransactionsState = Channel<FailedTransactionState>()
    val failedTransactionState = _failedTransactionsState.receiveAsFlow()

    private val _missingPaymentsState = Channel<MissingPaymentsState>()
    val missingPaymentsState = _missingPaymentsState.receiveAsFlow()

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
    fun fetchPendingMonthlyTransactions(action: String) {
        getPendingMonthlyTransactionsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pendingTransactionsState.send(
                        PendingTransactionsState(pendingTransactions = result.data)
                    )
                }
                is Resource.Loading -> {
                    _pendingTransactionsState.send(PendingTransactionsState(isLoading = true))
                }
                is Resource.Error -> {
                    _pendingTransactionsState.send(
                        PendingTransactionsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun fetchFailedMonthlyTransactions(action: String) {
        getFailedTransactionsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _failedTransactionsState.send(
                        FailedTransactionState(failedTransactions = result.data)
                    )
                }
                is Resource.Loading -> {
                    _failedTransactionsState.send(FailedTransactionState(isLoading = true))
                }
                is Resource.Error -> {
                    _failedTransactionsState.send(
                        FailedTransactionState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun fetchMissingPayments(action: String) {
        getMissingPaymentsUseCase(action).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _missingPaymentsState.send(
                        MissingPaymentsState(missingPayments = result.data)
                    )
                }
                is Resource.Loading -> {
                    _missingPaymentsState.send(MissingPaymentsState(isLoading = true))
                }
                is Resource.Error -> {
                    _missingPaymentsState.send(
                        MissingPaymentsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}