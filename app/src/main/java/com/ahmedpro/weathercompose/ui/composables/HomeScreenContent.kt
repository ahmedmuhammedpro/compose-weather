package com.ahmedpro.weathercompose.ui.composables

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.weathercompose.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    backgroundImageUri: Uri? = null,
    backgroundImageDate: String? = null,
    isLoading: Boolean = true,
    isLocationEnable: Boolean = false,
    weatherData: WeatherData? = null,
    lottieCompositionSpec: LottieCompositionSpec,
    onSettingClick: () -> Unit,
    onRetry: () -> Unit,
) {

    val lottieClearNight by rememberLottieComposition(lottieCompositionSpec)

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

        Icon(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopEnd)
                .clickable { onSettingClick.invoke() },
            imageVector = Icons.Rounded.Settings,
            contentDescription = null
        )
        if (weatherData != null) {
            Row(modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))

                LottieAnimation(
                    modifier = Modifier.weight(1f),
                    composition = lottieClearNight,
                    iterations = LottieConstants.IterateForever,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 52.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)
            ) {
                Text(
                    text = "${weatherData.main.temp.toInt()}°",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    modifier = modifier.padding(top = 18.dp),
                    text = weatherData.weather.firstOrNull()?.main ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = weatherData.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = if (isLocationEnable) R.drawable.ic_location_on else R.drawable.ic_location_off),
                        tint = Color.Black,
                        contentDescription = null
                    )
                }

                Text(
                    text = "${weatherData.main.tempMin.toInt()}°/${weatherData.main.tempMax.toInt()}° Feels like ${weatherData.main.feelsLike.toInt()}°",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        } else {
            Button(
                modifier = Modifier.align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF81D4FA)),
                onClick = onRetry,
            ) {
                Text("Retry", color = Color.Black)
            }
        }
    }
}