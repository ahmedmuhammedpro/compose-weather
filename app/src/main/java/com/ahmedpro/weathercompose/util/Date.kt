package com.ahmedpro.weathercompose.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun isDayTime(
    currentTime: Long,
    sunriseTime: Long,
    sunsetTime: Long,
    timeZoneOffset: Int
): Boolean {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("GMT")

    val currentLocalTime = Date(currentTime * 1000 + timeZoneOffset * 1000L)
    val sunriseLocalTime = Date(sunriseTime * 1000 + timeZoneOffset * 1000L)
    val sunsetLocalTime = Date(sunsetTime * 1000 + timeZoneOffset * 1000L)

    return currentLocalTime.after(sunriseLocalTime) && currentLocalTime.before(sunsetLocalTime)
}

