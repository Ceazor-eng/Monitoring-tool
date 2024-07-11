package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import com.monitoringtendepay.domain.repository.CompleteMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.FailedTransactionsRepository
import com.monitoringtendepay.domain.repository.FilterPaymentsRepository
import com.monitoringtendepay.domain.repository.MissingPaymentsRepository
import com.monitoringtendepay.domain.repository.PendingMonthlyTransactionsRepository
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetAllUssdSessionsUseCase
import com.monitoringtendepay.domain.usecase.GetCompleteMonthlyTransactionsUseCase
import com.monitoringtendepay.domain.usecase.GetFailedTransactionsUseCase
import com.monitoringtendepay.domain.usecase.GetFilteredPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetMissingPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetPendingMonthlyTransactionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetAllPaymentsUseCase(repository: AllPaymentsRepository): GetAllPaymentsUseCase {
        return GetAllPaymentsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAllUssdSessionsUseCase(repository: UssdSessionsRepository): GetAllUssdSessionsUseCase {
        return GetAllUssdSessionsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetFilteredPaymentsUseCase(repository: FilterPaymentsRepository): GetFilteredPaymentsUseCase {
        return GetFilteredPaymentsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetPendingMonthlyTransactionsUseCase(repository: PendingMonthlyTransactionsRepository): GetPendingMonthlyTransactionsUseCase {
        return GetPendingMonthlyTransactionsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetCompleteMonthlyTransactionsUseCase(repository: CompleteMonthlyTransactionsRepository): GetCompleteMonthlyTransactionsUseCase {
        return GetCompleteMonthlyTransactionsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetFailedMonthlyTransactionsUseCase(repository: FailedTransactionsRepository): GetFailedTransactionsUseCase {
        return GetFailedTransactionsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMissingPaymentsUseCase(repository: MissingPaymentsRepository): GetMissingPaymentsUseCase {
        return GetMissingPaymentsUseCase(repository)
    }
}