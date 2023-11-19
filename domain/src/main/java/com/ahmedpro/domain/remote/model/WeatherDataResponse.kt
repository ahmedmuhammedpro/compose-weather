package com.ahmedpro.domain.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherDataResponse(
    val coord: CoordResponse,
    val weather: List<WeatherResponse>,
    val base: String,
    val main: MainResponse,
    val visibility: Int,
    val wind: WindResponse,
    val rain: RainResponse?,
    val clouds: CloudsResponse,
    val dt: Long,
    val sys: SysResponse,
    val timezone: Int,
    val id: Long,
    val name: String,
    override val cod: Int,
    override val message: String? = null,
) : BaseResponse


data class CoordResponse(
    val lon: Double,
    val lat: Double
)


data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)


data class MainResponse(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val grndLevel: Int
)


data class WindResponse(
    val speed: Double,
    val deg: Int,
    val gust: Double
)


data class RainResponse(@SerializedName("1h") val rainForLatHour: Double)

data class CloudsResponse(val all: Int)

data class SysResponse(
    val type: Int,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)