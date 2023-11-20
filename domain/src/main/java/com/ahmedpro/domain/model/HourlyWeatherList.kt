package com.ahmedpro.domain.model

data class HourlyWeatherList(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val hourly: List<HourlyData>,
)

data class HourlyData(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>
)
