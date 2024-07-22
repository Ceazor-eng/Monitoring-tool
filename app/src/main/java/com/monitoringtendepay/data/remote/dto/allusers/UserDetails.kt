package com.monitoringtendepay.data.remote.dto.allusers

data class UserDetails(
    val createdBy: String,
    val createdAt: String,
    val firstName: String,
    val lastName: String,
    val modifiedAt: String,
    val phoneNumber: String,
    val role: String,
    val status: String,
    val username: String
)