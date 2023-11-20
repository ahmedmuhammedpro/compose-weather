package com.ahmedpro.domain.remote.service

import com.ahmedpro.domain.remote.model.HourlyWeatherListResponse
import com.ahmedpro.domain.remote.model.WeatherDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") lat: Float, @Query("lon") lon: Float): Response<WeatherDataResponse>

    @GET("onecall")
    suspend fun getHourlyWeatherList(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("exclude") exclude: String = "current,minutely,daily,alerts"
    ): Response<HourlyWeatherListResponse>
}

internal const val WEATHER_API_BASE_URL="https://api.openweathermap.org/data/2.5/"
internal const val WEATHER_API_KEY="0673564e144c39a9a5255747b5188355"