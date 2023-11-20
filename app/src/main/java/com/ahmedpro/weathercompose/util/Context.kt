package com.ahmedpro.weathercompose.util

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.requestEnableLocation() {
    this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}