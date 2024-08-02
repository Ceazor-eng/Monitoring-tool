package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.monthlytransactions.FailedMonthlyTransactionsDto

@Entity(tableName = "failed_transactions")
data class FailedTransactionsEntity(
    @PrimaryKey val failedPayments: String,
    val timestamp: Long // Add a timestamp field
)

fun FailedTransactionsEntity.toFailedTransactionsDto(): FailedMonthlyTransactionsDto {
    return FailedMonthlyTransactionsDto(
        failedPayments,
        timestamp
    )
}