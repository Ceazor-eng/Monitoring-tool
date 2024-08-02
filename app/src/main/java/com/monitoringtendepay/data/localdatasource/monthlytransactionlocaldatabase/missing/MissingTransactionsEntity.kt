package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.monthlytransactions.MissingPaymentsDto

@Entity(tableName = "missing_transactions")
data class MissingTransactionsEntity(
    @PrimaryKey val missingPayments: String,
    val timestamp: Long // Add a timestamp field
)

fun MissingTransactionsEntity.toMissingMonthlyTransactionsDto(): MissingPaymentsDto {
    return MissingPaymentsDto(
        missingPayments,
        timestamp
    )
}