package com.monitoringtendepay.data.remote.dto.ussd

data class FetchUssdSessions(
    val `data`: List<UssdData>,
    val totalSessions: String
)