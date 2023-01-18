package com.listingwithpagination.di

import com.google.gson.GsonBuilder
import com.listingwithpagination.BuildConfig
import com.listingwithpagination.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(base: String): ApiService =
        Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()))
            .build()
            .create(ApiService::class.java)
}