package com.monitoringtendepay.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.monitoringtendepay.domain.usecase.GetFilteredPaymentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterPaymentsViewModel @Inject constructor(
    private val getFilteredPaymentsUseCase: GetFilteredPaymentsUseCase
) :ViewModel(){
}