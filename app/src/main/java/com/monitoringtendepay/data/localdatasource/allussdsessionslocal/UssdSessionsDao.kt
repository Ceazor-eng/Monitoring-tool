package com.monitoringtendepay.data.localdatasource.allussdsessionslocal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UssdSessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessions(ussdSessions: List<AllSessionsEntity>)

    @Query("SELECT * FROM all_ussd_sessions WHERE timestamp > :validTime ")
    suspend fun getAllUssdSessions(validTime: Long): List<AllSessionsEntity>

    @Query("DELETE FROM all_ussd_sessions")
    suspend fun clearSessions()
}