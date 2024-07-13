package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.usecase.GetFilteredPaymentsUseCase
import com.monitoringtendepay.presentation.states.FilteredPaymentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class FilterPaymentsViewModel @Inject constructor(
    private val getFilteredPaymentsUseCase: GetFilteredPaymentsUseCase
) : ViewModel() {

    private val _serviceCodes = MutableLiveData<List<String>>().apply {
        value = listOf("mpesa", "bank")
    }
    val serviceCodes: LiveData<List<String>> = _serviceCodes

    private val _statusCodes = MutableLiveData<List<String>>().apply {
        value = listOf("1", "2", "3", "4")
    }
    val statusCodes: LiveData<List<String>> = _statusCodes

    private val _filteredPaymentsState = Channel<FilteredPaymentsState>()
    val paymentState = _filteredPaymentsState.receiveAsFlow()

    fun filterPayments(action: String, serviceType: String, status: String, startDate: String, endDate: String) {
        getFilteredPaymentsUseCase(action, serviceType, status, startDate, endDate).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _filteredPaymentsState.send(
                        FilteredPaymentsState(filterPayments = result.data ?: emptyList())
                    )
                }
                is Resource.Loading -> {
                    _filteredPaymentsState.send(FilteredPaymentsState(isLoading = true))
                }
                is Resource.Error -> {
                    _filteredPaymentsState.send(
                        FilteredPaymentsState(error = result.message ?: "An unexpected error occurred")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}