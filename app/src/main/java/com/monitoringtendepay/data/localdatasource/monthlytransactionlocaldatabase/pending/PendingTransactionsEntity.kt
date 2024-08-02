package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.monthlytransactions.PendingMonthlyTransactionsDto

@Entity(tableName = "pending_transactions")
data class PendingTransactionsEntity(
    @PrimaryKey val pendingPayments: String,
    val timestamp: Long // Add a timestamp field
)

fun PendingTransactionsEntity.toPendingMonthlyTransactionsDto(): PendingMonthlyTransactionsDto {
    return PendingMonthlyTransactionsDto(
        pendingPayments,
        timestamp
    )
}