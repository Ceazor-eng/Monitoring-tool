package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MissingTransactionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissingTransactions(missingTransactions: MissingTransactionsEntity)

    @Query("SELECT * FROM missing_transactions  WHERE timestamp > :validTime")
    suspend fun getAllMissingTransactions(validTime: Long): MissingTransactionsEntity

    @Query("DELETE FROM missing_transactions")
    suspend fun clearMissingTransactions()
}