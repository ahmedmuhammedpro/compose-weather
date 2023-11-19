package com.ahmedpro.domain.utils

import com.ahmedpro.domain.model.Clouds
import com.ahmedpro.domain.model.Coord
import com.ahmedpro.domain.model.Main
import com.ahmedpro.domain.model.Rain
import com.ahmedpro.domain.model.Sys
import com.ahmedpro.domain.model.Weather
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.model.Wind
import com.ahmedpro.domain.remote.model.CloudsResponse
import com.ahmedpro.domain.remote.model.CoordResponse
import com.ahmedpro.domain.remote.model.MainResponse
import com.ahmedpro.domain.remote.model.RainResponse
import com.ahmedpro.domain.remote.model.SysResponse
import com.ahmedpro.domain.remote.model.WeatherDataResponse
import com.ahmedpro.domain.remote.model.WeatherResponse
import com.ahmedpro.domain.remote.model.WindResponse

fun WeatherDataResponse.toWeatherData() = WeatherData(
    coord = coord.toCoord(),
    weather = weather.map { it.toWeather() },
    base = base,
    main = main.toMain(),
    visibility = visibility,
    wind = wind.toWind(),
    rain = rain?.toRain(),
    clouds = clouds.toClouds(),
    dt = dt,
    sys = sys.toSys(),
    timezone = timezone,
    id = id,
    name = name
)

fun CoordResponse.toCoord() = Coord(lon = lon, lat = lat)

fun WeatherResponse.toWeather() =
    Weather(id = id, main = main, description = description, icon = icon)

fun MainResponse.toMain() = Main(
    temp = temp,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax,
    pressure = pressure,
    humidity = humidity,
    seaLevel = seaLevel,
    grndLevel = grndLevel
)

fun Main.convertFromKelvinToCelsius() = this.copy(
    temp = temp.kelvinToCelsius(),
    feelsLike = feelsLike.kelvinToCelsius(),
    tempMin = tempMin.kelvinToCelsius(),
    tempMax = tempMax.kelvinToCelsius(),
)

fun Main.convertFromKelvinToFahrenheit() = this.copy(
    temp = temp.kelvinToFahrenheit(),
    feelsLike = feelsLike.kelvinToFahrenheit(),
    tempMin = tempMin.kelvinToFahrenheit(),
    tempMax = tempMax.kelvinToFahrenheit(),
)

fun Main.convertFromFahrenheitToCelsius() = this.copy(
    temp = temp.fahrenheitToCelsius(),
    feelsLike = feelsLike.fahrenheitToCelsius(),
    tempMin = tempMin.fahrenheitToCelsius(),
    tempMax = tempMax.fahrenheitToCelsius(),
)

fun Main.convertFromCelsiusToFahrenheit() = this.copy(
    temp = temp.celsiusToFahrenheit(),
    feelsLike = feelsLike.celsiusToFahrenheit(),
    tempMin = tempMin.celsiusToFahrenheit(),
    tempMax = tempMax.celsiusToFahrenheit(),
)

fun WindResponse.toWind() = Wind(speed = speed, deg = deg, gust = gust)

fun RainResponse?.toRain() = this?.let { Rain(rainForLatHour = it.rainForLatHour) }

fun CloudsResponse.toClouds() = Clouds(all = all)

fun SysResponse.toSys() =
    Sys(type = type, id = id, country = country, sunrise = sunrise, sunset = sunset)

fun Int.isSuccessful() = this in 200..299

fun Double.kelvinToCelsius() = this - 273.15

fun Double.kelvinToFahrenheit() = (this - 273.15) * 9 / 5 + 32

fun Double.fahrenheitToCelsius() = (this - 32) * 5 / 9

fun Double.celsiusToFahrenheit() = this * 9 / 5 + 32