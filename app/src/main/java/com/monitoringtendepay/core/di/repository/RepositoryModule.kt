package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.repository.AllPaymentsRepositoryImpl
import com.monitoringtendepay.data.repository.AllUssdSessionsRepositoryImpl
import com.monitoringtendepay.data.repository.CompleteMonthlyTransactionsImpl
import com.monitoringtendepay.data.repository.FailedTransactionsRepositoryImpl
import com.monitoringtendepay.data.repository.FilterPaymentsRepositoryImpl
import com.monitoringtendepay.data.repository.PendingMonthlyTransactionsRepositoryImpl
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import com.monitoringtendepay.domain.repository.FilterPaymentsRepository
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAllPaymentsRepository(api: AllPaymentsApi): AllPaymentsRepository {
        return AllPaymentsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideUssdSessionsRepository(api: AllPaymentsApi): UssdSessionsRepository {
        return AllUssdSessionsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideFilteredPaymentsRepository(api: AllPaymentsApi): FilterPaymentsRepository {
        return FilterPaymentsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideCompleteTransactionsRepository(api: AllPaymentsApi): CompleteMonthlyTransactionsRepository {
        return CompleteMonthlyTransactionsImpl(api)
    }

    @Singleton
    @Provides
    fun providePendingTransactionsRepository(api: AllPaymentsApi): PendingMonthlyTransactionsRepository {
        return PendingMonthlyTransactionsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideFailedTransactionsRepository(api: AllPaymentsApi): FailedTransactionsRepository {
        return FailedTransactionsRepositoryImpl(api)
    }
}