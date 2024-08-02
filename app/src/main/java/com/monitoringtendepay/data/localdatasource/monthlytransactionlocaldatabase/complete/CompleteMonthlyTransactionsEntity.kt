package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monitoringtendepay.data.remote.dto.monthlytransactions.TotalMonthlyTransactionsDto

@Entity(tableName = "all_complete_monthly_transactions")
data class CompleteMonthlyTransactionsEntity(
    @PrimaryKey val completePayments: String,
    val timestamp: Long // Add a timestamp field
)

fun CompleteMonthlyTransactionsEntity.toTotalMonthlyTransactionsDto(): TotalMonthlyTransactionsDto {
    return TotalMonthlyTransactionsDto(
        completePayments,
        timestamp
    )
}