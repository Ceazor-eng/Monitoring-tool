package com.monitoringtendepay.data.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.payments.FetchAllPaymentsDto

@Entity(tableName = "all_payments")
data class AllPaymentsEntity(
    @PrimaryKey val id: String,
    val amount: String,
    val groupId: String,
    val initiatorName: String,
    val initiatorPhone: String,
    val internalRef: String,
    val mpesaRef: String,
    val paymentStatus: String,
    val paymentStatusMessage: String,
    val salesforcePhone: String,
    val serviceCode: String,
    val sessionId: String,
    val transactionDate: String
)

fun AllPaymentsEntity.toFetchAllPaymentsDto(): FetchAllPaymentsDto {
    return FetchAllPaymentsDto(
        id,
        amount,
        groupId,
        initiatorName,
        initiatorPhone,
        internalRef,
        mpesaRef,
        paymentStatus,
        paymentStatusMessage,
        salesforcePhone,
        serviceCode,
        sessionId,
        transactionDate
    )
}