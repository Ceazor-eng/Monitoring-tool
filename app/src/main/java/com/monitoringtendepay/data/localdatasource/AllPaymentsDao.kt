package com.monitoringtendepay.data.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AllPaymentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<AllPaymentsEntity>)

    @Query("SELECT * FROM all_payments")
    suspend fun getAllPayments(): List<AllPaymentsEntity>

    @Query("DELETE FROM all_payments")
    suspend fun clearPayments()
}
// ORDER BY transactionDate DESC LIMIT 3