package com.monitoringtendepay.core.di.network

import com.monitoringtendepay.core.common.Constants.BASE_URL
import com.monitoringtendepay.data.remote.apiservice.AllPaymentsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesAllPaymentsApi(): AllPaymentsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AllPaymentsApi::class.java)
    }
}