package com.monitoringtendepay.data.remote.dto.useractions.deactivate

data class DeactivateUserRequest(
    val action: String,
    val username: String
)