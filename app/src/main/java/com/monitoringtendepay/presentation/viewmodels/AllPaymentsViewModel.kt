package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.AllPayments
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
    private val getMissingPaymentsUseCase: GetMissingPaymentsUseCase,
    private val savedStateHandle: SavedStateHandle
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

    // Cached data
    private var cachedPayments: List<AllPayments>? = null
    private var cachedCompleteTransactions: String? = null
    private var cachedPendingTransactions: String? = null
    private var cachedFailedTransactions: String? = null
    private var cachedMissingPayments: String? = null

    suspend fun fetchAllPayments(action: String) {
        if (cachedPayments == null) {
            getAllPaymentsUseCase(action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        cachedPayments = result.data ?: emptyList()
                        _allPaymentsState.send(
                            AllPaymentsState(payments = cachedPayments!!)
                        )
                    }
                    is Resource.Loading -> {
                        _allPaymentsState.send(AllPaymentsState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _allPaymentsState.send(
                            AllPaymentsState(error = result.message ?: "An unexpected error occurred")
                        )
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            _allPaymentsState.send(AllPaymentsState(payments = cachedPayments!!))
        }
    }

    suspend fun fetchCompleteMonthlyTransactions(action: String) {
        if (cachedCompleteTransactions == null) {
            getCompleteMonthlyTransactionsUseCase(action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        cachedCompleteTransactions = result.data.toString()
                        _completeTransactionsState.send(
                            CompleteTransactionsState(completeTransactions = cachedCompleteTransactions)
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
        } else {
            _completeTransactionsState.send(CompleteTransactionsState(completeTransactions = cachedCompleteTransactions))
        }
    }

    suspend fun fetchPendingMonthlyTransactions(action: String) {
        if (cachedPendingTransactions == null) {
            getPendingMonthlyTransactionsUseCase(action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        cachedPendingTransactions = result.data.toString()
                        _pendingTransactionsState.send(
                            PendingTransactionsState(pendingTransactions = cachedPendingTransactions)
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
        } else {
            _pendingTransactionsState.send(PendingTransactionsState(pendingTransactions = cachedPendingTransactions))
        }
    }

    suspend fun fetchFailedMonthlyTransactions(action: String) {
        if (cachedFailedTransactions == null) {
            getFailedTransactionsUseCase(action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        cachedFailedTransactions = result.data.toString()
                        _failedTransactionsState.send(
                            FailedTransactionState(failedTransactions = cachedFailedTransactions)
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
        } else {
            _failedTransactionsState.send(FailedTransactionState(failedTransactions = cachedFailedTransactions))
        }
    }

    suspend fun fetchMissingPayments(action: String) {
        if (cachedMissingPayments == null) {
            getMissingPaymentsUseCase(action).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        cachedMissingPayments = result.data.toString()
                        _missingPaymentsState.send(
                            MissingPaymentsState(missingPayments = cachedMissingPayments)
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
        } else {
            _missingPaymentsState.send(MissingPaymentsState(missingPayments = cachedMissingPayments))
        }
    }
}