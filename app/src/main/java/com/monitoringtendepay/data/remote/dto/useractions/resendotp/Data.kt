package com.monitoringtendepay.data.remote.dto.useractions.resendotp

data class Data(
    val message: String,
    val newOtp: Int,
    val phoneNumber: String,
    val status: String,
    val username: String
)