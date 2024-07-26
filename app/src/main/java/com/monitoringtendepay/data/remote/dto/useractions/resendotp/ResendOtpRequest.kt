package com.monitoringtendepay.data.remote.dto.useractions.resendotp

data class ResendOtpRequest(
    val action: String,
    val username: String
)