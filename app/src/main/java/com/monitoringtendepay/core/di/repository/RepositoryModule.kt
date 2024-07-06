package com.monitoringtendepay.core.di.repository

import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import com.monitoringtendepay.data.repository.AllPaymentsRepositoryImpl
import com.monitoringtendepay.domain.repository.AllPaymentsRepository
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
}