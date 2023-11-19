package com.ahmedpro.domain.repository

import com.ahmedpro.domain.base.ErrorDetails
import com.ahmedpro.domain.base.Result
import com.ahmedpro.domain.base.request
import com.ahmedpro.domain.datastore.PrefStore
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.remote.service.WeatherService
import com.ahmedpro.domain.utils.TempType
import com.ahmedpro.domain.utils.convertFromKelvinToCelsius
import com.ahmedpro.domain.utils.convertFromKelvinToFahrenheit
import com.ahmedpro.domain.utils.isSuccessful
import com.ahmedpro.domain.utils.toWeatherData
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val prefStore: PrefStore,
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Float, lon: Float): Result<WeatherData> {
        return request {
            val weatherDataResult = weatherService.getCurrentWeather(lat, lon)
            if (weatherDataResult.cod.isSuccessful()) {
                val tempType = prefStore.tempType.firstOrNull()
                val convertedTemps =
                    if (tempType == TempType.CELSIUS) {
                        weatherDataResult.toWeatherData().main.convertFromKelvinToCelsius()
                    } else {
                        weatherDataResult.toWeatherData().main.convertFromKelvinToFahrenheit()
                    }
                val weatherData = weatherDataResult.toWeatherData().copy(main = convertedTemps)
                prefStore.saveWeatherData(weatherData)
                Result.Success(data = weatherData)
            }
            else {
                Result.Error(errorDetails = ErrorDetails(code = weatherDataResult.cod, message = weatherDataResult.message))
            }
        }
    }

    override suspend fun getCachedWeather() = prefStore.cachedWeatherData.firstOrNull()

    override suspend fun getTempType(): TempType = prefStore.tempType.firstOrNull() ?: TempType.CELSIUS

    override suspend fun useDefaultBackground(): Boolean = prefStore.useDefaultBackground.firstOrNull() ?: true

    override suspend fun saveTempType(tempType: TempType) = prefStore.saveTempType(tempType)

    override suspend fun saveUseDefaultBackground(value: Boolean) = prefStore.saveUsingDefaultBackground(value)

}