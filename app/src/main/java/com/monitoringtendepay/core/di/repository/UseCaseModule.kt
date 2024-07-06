package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.domain.repository.AllPaymentsRepository
import com.monitoringtendepay.domain.usecase.GetAllPaymentsUseCase
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
}