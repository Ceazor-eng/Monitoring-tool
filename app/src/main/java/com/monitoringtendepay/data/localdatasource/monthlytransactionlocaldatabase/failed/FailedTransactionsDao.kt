package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FailedTransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailedTransactions(failedTransactions: FailedTransactionsEntity)

    @Query("SELECT * FROM failed_transactions WHERE timestamp > :validTime")
    suspend fun getAllFailedTransactions(validTime: Long): FailedTransactionsEntity

    @Query("DELETE FROM failed_transactions")
    suspend fun clearFailedTransactions()
}