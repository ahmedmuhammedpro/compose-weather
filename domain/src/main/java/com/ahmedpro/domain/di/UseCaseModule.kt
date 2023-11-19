package com.ahmedpro.domain.di

import com.ahmedpro.domain.repository.WeatherRepository
import com.ahmedpro.domain.usecase.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun providesWeatherCases(weatherRepository: WeatherRepository): WeatherUseCases =
        WeatherUseCases((weatherRepository))

    @Provides
    fun providesGetCurrentWeatherUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.GetCurrentWeatherUseCase =
        weatherUseCases.getCurrentWeatherUseCase

    @Provides
    fun providesGetCachedWeatherUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.GetCachedWeatherUseCase =
        weatherUseCases.getCachedWeatherUseCase

    @Provides
    fun providesGetTempTypeUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.GetTempTypeUseCase =
        weatherUseCases.getTempTypeUseCase

    @Provides
    fun providesGetUseDefaultBackgroundUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.GetUseDefaultBackgroundUseCase =
        weatherUseCases.getUseDefaultBackgroundUseCase

    @Provides
    fun providesSaveTempTypeUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.SaveTempTypeUseCase =
        weatherUseCases.saveTempTypeUseCase

    @Provides
    fun providesSaveUseDefaultBackgroundUseCase(weatherUseCases: WeatherUseCases): WeatherUseCases.SaveUseDefaultBackgroundUseCase =
        weatherUseCases.saveUseDefaultBackgroundUseCase
}