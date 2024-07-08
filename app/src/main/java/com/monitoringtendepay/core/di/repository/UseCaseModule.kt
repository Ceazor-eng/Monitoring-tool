package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import com.monitoringtendepay.domain.repository.UssdSessionsRepository
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
import com.monitoringtendepay.domain.usecase.GetAllUssdSessionsUseCase
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
}