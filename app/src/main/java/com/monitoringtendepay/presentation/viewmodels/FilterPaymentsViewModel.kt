package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.monitoringtendepay.domain.usecase.GetFilteredPaymentsUseCase
import com.monitoringtendepay.presentation.states.FilteredPaymentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class FilterPaymentsViewModel @Inject constructor(
    private val getFilteredPaymentsUseCase: GetFilteredPaymentsUseCase
) : ViewModel() {
    private val _filteredPaymentsState = Channel<FilteredPaymentsState>()
    val paymentState = _filteredPaymentsState.receiveAsFlow()
}