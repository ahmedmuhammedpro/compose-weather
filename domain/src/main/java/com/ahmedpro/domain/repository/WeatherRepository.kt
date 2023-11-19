package com.ahmedpro.domain.repository

import com.ahmedpro.domain.base.Result
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.utils.TempType

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Float, lon: Float): Result<WeatherData>
    suspend fun getCachedWeather(): WeatherData?
    suspend fun getTempType(): TempType
    suspend fun useDefaultBackground(): Boolean

    suspend fun saveTempType(tempType: TempType)
    suspend fun saveUseDefaultBackground(value: Boolean)
}