package com.ahmedpro.domain.di

import com.ahmedpro.domain.datastore.PrefStore
import com.ahmedpro.domain.repository.WeatherRepository
import com.ahmedpro.domain.repository.WeatherRepositoryImpl
import com.ahmedpro.domain.remote.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesWeatherRepository(weatherService: WeatherService, prefStore: PrefStore): WeatherRepository {
        return WeatherRepositoryImpl(weatherService, prefStore)
    }
}