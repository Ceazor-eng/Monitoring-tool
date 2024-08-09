package com.monitoringtendepay.data.localdatasource.allussdsessionslocal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AllSessionsEntity::class], version = 2, exportSchema = false)
abstract class UssdSessionsDatabase : RoomDatabase() {
    abstract fun ussdSessionsDao(): UssdSessionsDao
}