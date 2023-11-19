package com.ahmedpro.weathercompose.util

import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.weathercompose.R
import com.airbnb.lottie.compose.LottieCompositionSpec

fun getWeatherLottieRes(weatherData: WeatherData?): LottieCompositionSpec {
    if (weatherData == null) return LottieCompositionSpec.RawRes(R.raw.lottie_clear_night)
    val isDay = isDayTime(
        currentTime = System.currentTimeMillis() / 1000,
        sunriseTime = weatherData.sys.sunrise,
        sunsetTime = weatherData.sys.sunset,
        timeZoneOffset = weatherData.timezone
    )
    val resValue =
        if (isDay) {
            when (weatherData.weather.firstOrNull()?.main?.uppercase() ?: "") {
                WeatherStatus.CLEAR.name -> R.raw.lottie_clouds_day
                WeatherStatus.CLOUDS.name -> R.raw.lottie_clouds_day
                WeatherStatus.SNOW.name -> R.raw.lottie_snow_day

                WeatherStatus.RAIN.name, WeatherStatus.DRIZZLE.name,
                WeatherStatus.THUNDERSTORM.name, WeatherStatus.FOG.name -> R.raw.lottie_rain_day

                WeatherStatus.MIST.name, WeatherStatus.SMOKE.name,
                WeatherStatus.HAZE.name -> R.raw.lottie_mist_day

                else -> R.raw.lottie_clear_day
            }
        } else {
            when (weatherData.weather.firstOrNull()?.main?.uppercase() ?: "") {
                WeatherStatus.CLEAR.name -> R.raw.lottie_clouds_night
                WeatherStatus.CLOUDS.name -> R.raw.lottie_clouds_night
                WeatherStatus.SNOW.name -> R.raw.lottie_snow_night

                WeatherStatus.RAIN.name, WeatherStatus.DRIZZLE.name,
                WeatherStatus.THUNDERSTORM.name, WeatherStatus.FOG.name -> R.raw.lottie_rain_night

                WeatherStatus.MIST.name, WeatherStatus.SMOKE.name,
                WeatherStatus.HAZE.name -> R.raw.lottie_mist_night

                else -> R.raw.lottie_clear_night
            }
        }

    return LottieCompositionSpec.RawRes(resValue)
}