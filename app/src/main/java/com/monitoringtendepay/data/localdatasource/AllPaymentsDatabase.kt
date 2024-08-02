package com.monitoringtendepay.data.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AllPaymentsEntity::class], version = 2, exportSchema = false)
abstract class AllPaymentsDatabase : RoomDatabase() {
    abstract fun allPaymentsDao(): AllPaymentsDao
}