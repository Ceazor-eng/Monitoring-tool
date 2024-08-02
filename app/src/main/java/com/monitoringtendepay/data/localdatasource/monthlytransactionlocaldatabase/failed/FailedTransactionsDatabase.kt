package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FailedTransactionsEntity::class], version = 3, exportSchema = false)
abstract class FailedTransactionsDatabase : RoomDatabase() {
    abstract fun failedMonthlyTransactionsDao(): FailedTransactionsDao
}