package com.monitoringtendepay.domain.models

data class FilterUssdSessionsParams(
    val msisdn: String,
    val sessionId: String,
    val sessionsDate: String
)