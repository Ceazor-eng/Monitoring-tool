package com.monitoringtendepay.data.remote.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val status: String,
    val message: String,
    @SerializedName("role") val role: String,
    @SerializedName("userName") val username: String,
    val salutation: String,
    val sessionToken: String
)