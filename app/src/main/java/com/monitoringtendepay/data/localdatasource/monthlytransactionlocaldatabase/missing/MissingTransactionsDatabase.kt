package com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MissingTransactionsEntity::class], version = 3, exportSchema = false)
abstract class MissingTransactionsDatabase : RoomDatabase() {
    abstract fun missingTransactionsDao(): MissingTransactionsDao
}