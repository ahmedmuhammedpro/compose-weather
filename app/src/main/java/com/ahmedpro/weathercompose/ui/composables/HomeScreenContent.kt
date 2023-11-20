package com.ahmedpro.weathercompose.ui.composables

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahmedpro.domain.model.HourlyWeatherList
import com.ahmedpro.domain.model.WeatherData
import com.airbnb.lottie.compose.LottieCompositionSpec

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    backgroundImageUri: Uri? = null,
    backgroundImageDate: String? = null,
    isLoading: Boolean = true,
    isLocationEnable: Boolean = false,
    weatherData: WeatherData? = null,
    hourlyWeatherList: HourlyWeatherList? = null,
    lottieCompositionSpec: LottieCompositionSpec,
    onSettingClick: () -> Unit,
    onEnableLocationClick: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImageUri(
            uri = backgroundImageUri,
            date = backgroundImageDate
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                color = Color(0xFF3F51B5)
            )
        }

        if (isLocationEnable.not()) {
            EnableLocationView(onEnableLocationClick = onEnableLocationClick)
        }

        Icon(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopEnd)
                .clickable { onSettingClick.invoke() },
            imageVector = Icons.Rounded.Settings,
            contentDescription = null
        )

        if (weatherData != null) {
            WeatherDataView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 52.dp),
                weatherData = weatherData,
                hourlyWeatherList = hourlyWeatherList,
                lottieCompositionSpec = lottieCompositionSpec,
                isLocationEnable = isLocationEnable,
            )
        } else {
            Button(
                modifier = Modifier.align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                onClick = onRetry,
            ) {
                Text("Retry", color = Color.White)
            }
        }
    }
}