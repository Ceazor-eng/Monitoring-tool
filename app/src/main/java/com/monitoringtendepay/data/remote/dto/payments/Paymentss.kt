package com.monitoringtendepay.data.remote.dto.payments

import com.monitoringtendepay.data.remote.dto.payments.FetchAllPaymentsDto

data class Paymentss(
    val `data`: List<FetchAllPaymentsDto>,
    val totalPayments: String
)