package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompleteMonthlyTransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompleteMonthlyTransactions(completeTransactions: CompleteMonthlyTransactionsEntity)

    @Query("SELECT * FROM all_complete_monthly_transactions WHERE timestamp > :validTime ")
    suspend fun getAllCompleteTransactions(validTime: Long): CompleteMonthlyTransactionsEntity

    @Query("DELETE FROM all_complete_monthly_transactions")
    suspend fun clearCompleteTransactions()
}