package com.monitoringtendepay.data.localdatasource.allpaymentslocaldatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AllPaymentsEntity::class], version = 3, exportSchema = false)
abstract class AllPaymentsDatabase : RoomDatabase() {
    abstract fun allPaymentsDao(): AllPaymentsDao
}