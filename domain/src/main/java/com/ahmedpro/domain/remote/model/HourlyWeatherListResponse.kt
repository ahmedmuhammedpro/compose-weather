package com.ahmedpro.domain.remote.model

import com.google.gson.annotations.SerializedName

data class HourlyWeatherListResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    val hourly: List<HourlyDataResponse>,
)

data class HourlyDataResponse(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherResponse>
)