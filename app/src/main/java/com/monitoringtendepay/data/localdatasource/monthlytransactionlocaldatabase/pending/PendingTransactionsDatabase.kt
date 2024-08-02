package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PendingTransactionsEntity::class], version = 3, exportSchema = false)
abstract class PendingTransactionsDatabase : RoomDatabase() {
    abstract fun pendingTransactionsDao(): PendingTransactionsDao
}