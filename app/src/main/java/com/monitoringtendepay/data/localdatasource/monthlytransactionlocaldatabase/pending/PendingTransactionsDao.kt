package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PendingTransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingTransactions(pendingTransactions: PendingTransactionsEntity)

    @Query("SELECT * FROM pending_transactions WHERE timestamp > :validTime")
    suspend fun getAllPendingTransactions(validTime: Long): PendingTransactionsEntity

    @Query("DELETE FROM pending_transactions")
    suspend fun clearPendingTransactions()
}