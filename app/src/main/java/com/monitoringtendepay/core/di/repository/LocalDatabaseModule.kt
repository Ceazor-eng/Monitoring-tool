package com.monitoringtendepay.core.di.repository

import android.content.Context
import androidx.room.Room
import com.monitoringtendepay.data.localdatasource.allpaymentslocaldatabase.AllPaymentsDao
import com.monitoringtendepay.data.localdatasource.allpaymentslocaldatabase.AllPaymentsDatabase
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.CompleteMonthlyTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.CompleteMonthlyTransactionsDatabase
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.FailedTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.FailedTransactionsDatabase
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.MissingTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.MissingTransactionsDatabase
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.PendingTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.PendingTransactionsDatabase
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
    fun provideCompleteMonthlyTransactionsDao(database: CompleteMonthlyTransactionsDatabase): CompleteMonthlyTransactionsDao {
        return database.completeMonthlyTransactionsDao()
    }

    @Provides
    @Singleton
    fun provideFailedMonthlyTransactionsDao(database: FailedTransactionsDatabase): FailedTransactionsDao {
        return database.failedMonthlyTransactionsDao()
    }

    @Provides
    @Singleton
    fun providePendingMonthlyTransactionsDao(database: PendingTransactionsDatabase): PendingTransactionsDao {
        return database.pendingTransactionsDao()
    }

    @Provides
    @Singleton
    fun provideMissingMonthlyTransactionsDao(database: MissingTransactionsDatabase): MissingTransactionsDao {
        return database.missingTransactionsDao()
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

    @Provides
    @Singleton
    fun provideCompleteMonthlyTransactionsDatabase(@ApplicationContext context: Context): CompleteMonthlyTransactionsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CompleteMonthlyTransactionsDatabase::class.java,
            "all_complete_monthly_transactions"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFailedMonthlyTransactionsDatabase(@ApplicationContext context: Context): FailedTransactionsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FailedTransactionsDatabase::class.java,
            "failed_transactions"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePendingMonthlyTransactionsDatabase(@ApplicationContext context: Context): PendingTransactionsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PendingTransactionsDatabase::class.java,
            "pending_transactions"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMissingMonthlyTransactionsDatabase(@ApplicationContext context: Context): MissingTransactionsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MissingTransactionsDatabase::class.java,
            "missing_transactions"
        ).fallbackToDestructiveMigration()
            .build()
    }
}