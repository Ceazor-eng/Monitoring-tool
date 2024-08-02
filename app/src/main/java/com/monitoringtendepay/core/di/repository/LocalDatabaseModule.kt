package com.monitoringtendepay.core.di.repository

import android.content.Context
import androidx.room.Room
import com.monitoringtendepay.data.localdatasource.AllPaymentsDao
import com.monitoringtendepay.data.localdatasource.AllPaymentsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideAllPaymentsDao(database: AllPaymentsDatabase): AllPaymentsDao {
        return database.allPaymentsDao()
    }

    @Provides
    @Singleton
    fun provideAllPaymentsDatabase(@ApplicationContext context: Context): AllPaymentsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AllPaymentsDatabase::class.java,
            "All_Payments_Database"
        ).fallbackToDestructiveMigration()
            .build()
    }
}