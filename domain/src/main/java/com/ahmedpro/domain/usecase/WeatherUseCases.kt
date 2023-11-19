package com.ahmedpro.domain.usecase

import com.ahmedpro.domain.base.AsyncUseCase
import com.ahmedpro.domain.base.Result
import com.ahmedpro.domain.repository.WeatherRepository
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.utils.TempType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class WeatherUseCases @Inject constructor(private val weatherRepository: WeatherRepository) {

    class GetCurrentWeatherUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.RequestUseCaseParameters<GetCurrentWeatherUseCase.Params, Flow<Result<WeatherData>>> {
        data class Params(val lat: Float, val lon: Float)

        override suspend fun doWork(params: Params): Flow<Result<WeatherData>> {
            return flow {
                val result = weatherRepository.getCurrentWeather(params.lat, params.lon)
                emit(result)
            }.flowOn(Dispatchers.IO).onStart { Result.Loading }
        }
    }

    class GetCachedWeatherUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.RequestUseCase<WeatherData?> {
        override suspend fun doWork(): WeatherData? {
            return weatherRepository.getCachedWeather()
        }
    }

    class GetTempTypeUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.RequestUseCase<TempType> {
        override suspend fun doWork(): TempType {
            return weatherRepository.getTempType()
        }
    }

    class GetUseDefaultBackgroundUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.RequestUseCase<Boolean> {
        override suspend fun doWork(): Boolean {
            return weatherRepository.useDefaultBackground()
        }
    }

    class SaveTempTypeUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.PostUseCase<TempType> {
        override suspend fun doWork(parameter: TempType) {
            return weatherRepository.saveTempType(parameter)
        }
    }

    class SaveUseDefaultBackgroundUseCase internal constructor(
        private val weatherRepository: WeatherRepository
    ) : AsyncUseCase.PostUseCase<Boolean> {
        override suspend fun doWork(parameter: Boolean) {
            return weatherRepository.saveUseDefaultBackground(parameter)
        }
    }

    val getCurrentWeatherUseCase by lazy {
        GetCurrentWeatherUseCase(weatherRepository = weatherRepository)
    }

    val getCachedWeatherUseCase by lazy {
        GetCachedWeatherUseCase(weatherRepository = weatherRepository)
    }

    val getTempTypeUseCase by lazy {
        GetTempTypeUseCase(weatherRepository = weatherRepository)
    }

    val getUseDefaultBackgroundUseCase by lazy {
        GetUseDefaultBackgroundUseCase(weatherRepository = weatherRepository)
    }

    val saveTempTypeUseCase by lazy {
        SaveTempTypeUseCase(weatherRepository = weatherRepository)
    }

    val saveUseDefaultBackgroundUseCase by lazy {
        SaveUseDefaultBackgroundUseCase(weatherRepository = weatherRepository)
    }

}