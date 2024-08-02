package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompleteMonthlyTransactionsEntity::class], version = 5, exportSchema = false)
abstract class CompleteMonthlyTransactionsDatabase : RoomDatabase() {
    abstract fun completeMonthlyTransactionsDao(): CompleteMonthlyTransactionsDao
}