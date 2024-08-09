package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.data.localdatasource.allpaymentslocaldatabase.AllPaymentsDao
import com.monitoringtendepay.data.localdatasource.allussdsessionslocal.UssdSessionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.complete.CompleteMonthlyTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.failed.FailedTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.missing.MissingTransactionsDao
import com.monitoringtendepay.data.localdatasource.monthlytransactionlocaldatabase.pending.PendingTransactionsDao
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.remote.apiservice.AuthService
import com.monitoringtendepay.data.repository.AllPaymentsRepositoryImpl
import com.monitoringtendepay.data.repository.AllUsersRepositoryImpl
import com.monitoringtendepay.data.repository.AllUssdSessionsRepositoryImpl
import com.monitoringtendepay.data.repository.AuthRepositoryImpl
import com.monitoringtendepay.data.repository.CompleteMonthlyTransactionsImpl
import com.monitoringtendepay.data.repository.FailedTransactionsRepositoryImpl
import com.monitoringtendepay.data.repository.FilterPaymentsRepositoryImpl
import com.monitoringtendepay.data.repository.FilterUssdSessionsRepositoryImpl
import com.monitoringtendepay.data.repository.MissingPaymentsRepositoryImpl
import com.monitoringtendepay.data.repository.PendingMonthlyTransactionsRepositoryImpl
import com.monitoringtendepay.data.repository.UserActionsRepositoryImpl
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import com.monitoringtendepay.domain.repository.AllUsersRepository
import com.monitoringtendepay.domain.repository.AuthRepository
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import com.monitoringtendepay.domain.repository.FilterPaymentsRepository
import com.monitoringtendepay.domain.repository.FilterUssdSessionsRepository
import com.monitoringtendepay.domain.repository.MissingPaymentsRepository
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.UserActionsRepository
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
    fun provideAllPaymentsRepository(api: AllPaymentsApi, dao: AllPaymentsDao): AllPaymentsRepository {
        return AllPaymentsRepositoryImpl(api, dao)
    }

    @Singleton
    @Provides
    fun provideUssdSessionsRepository(api: AllPaymentsApi, ussdDao: UssdSessionsDao): UssdSessionsRepository {
        return AllUssdSessionsRepositoryImpl(api, ussdDao)
    }

    @Singleton
    @Provides
    fun provideFilteredPaymentsRepository(api: AllPaymentsApi): FilterPaymentsRepository {
        return FilterPaymentsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideFilteredUssdSessionsRepository(api: AllPaymentsApi): FilterUssdSessionsRepository {
        return FilterUssdSessionsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideCompleteTransactionsRepository(api: AllPaymentsApi, monthlyDao: CompleteMonthlyTransactionsDao): CompleteMonthlyTransactionsRepository {
        return CompleteMonthlyTransactionsImpl(api, monthlyDao)
    }

    @Singleton
    @Provides
    fun providePendingTransactionsRepository(api: AllPaymentsApi, pendingDao: PendingTransactionsDao): PendingMonthlyTransactionsRepository {
        return PendingMonthlyTransactionsRepositoryImpl(api, pendingDao)
    }

    @Singleton
    @Provides
    fun provideFailedTransactionsRepository(api: AllPaymentsApi, failedDao: FailedTransactionsDao): FailedTransactionsRepository {
        return FailedTransactionsRepositoryImpl(api, failedDao)
    }

    @Singleton
    @Provides
    fun provideMissingPaymentsRepository(api: AllPaymentsApi, missingDao: MissingTransactionsDao): MissingPaymentsRepository {
        return MissingPaymentsRepositoryImpl(api, missingDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideAllUsersRepository(authService: AuthService): AllUsersRepository {
        return AllUsersRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideUserActionsRepository(authService: AuthService): UserActionsRepository {
        return UserActionsRepositoryImpl(authService)
    }
}