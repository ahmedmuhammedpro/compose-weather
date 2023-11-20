package com.ahmedpro.weathercompose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ahmedpro.domain.model.HourlyWeatherList
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.weathercompose.R
import com.ahmedpro.weathercompose.util.IMAGE_URL
import com.ahmedpro.weathercompose.util.getFormattedHour
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun WeatherDataView(
    modifier: Modifier = Modifier,
    isLocationEnable: Boolean = false,
    weatherData: WeatherData,
    hourlyWeatherList: HourlyWeatherList? = null,
    lottieCompositionSpec: LottieCompositionSpec,
) {
    val lottieComposition by rememberLottieComposition(lottieCompositionSpec)

    Box(modifier = modifier) {
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))

            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "${weatherData.main.temp.toInt()}°",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 18.dp),
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

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                HourlyWeatherListCard(hourlyWeatherList = hourlyWeatherList)
            }
        }
    }
}

@Composable
fun HourlyWeatherListCard(
    modifier: Modifier = Modifier,
    hourlyWeatherList: HourlyWeatherList?
) {
    if (hourlyWeatherList == null) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x805C16D8),
                        Color(0x805F2CB9),
                        Color(0x80673AB7),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        LazyRow {
            items(hourlyWeatherList.hourly) { item ->
                Column(
                    modifier = Modifier.padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = getFormattedHour(item.dt),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    AsyncImage(
                        modifier = Modifier.size(48.dp),
                        model = "$IMAGE_URL/${item.weather.firstOrNull()?.icon}.png",
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "${item.temp.toInt()}°",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}