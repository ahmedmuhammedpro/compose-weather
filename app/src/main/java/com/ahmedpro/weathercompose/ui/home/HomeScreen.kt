package com.ahmedpro.weathercompose.ui.home

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmedpro.domain.utils.TempType
import com.ahmedpro.weathercompose.ui.composables.ErrorDialog
import com.ahmedpro.weathercompose.ui.composables.HomeScreenContent
import com.ahmedpro.weathercompose.ui.composables.SettingsDialog
import com.ahmedpro.weathercompose.ui.composables.permissions.LocationPermissionRequester
import com.ahmedpro.weathercompose.ui.composables.permissions.isLocationPermissionsEnabled
import com.ahmedpro.weathercompose.util.createImageUri
import com.ahmedpro.weathercompose.util.formatDate
import com.ahmedpro.weathercompose.util.getAllImagesUris
import com.ahmedpro.weathercompose.util.getFileDate
import com.ahmedpro.weathercompose.util.getWeatherLottieRes
import com.ahmedpro.weathercompose.util.requestEnableLocation
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val shouldRequestLocationPermission by remember {
            mutableStateOf(context.isLocationPermissionsEnabled().not())
        }
        var locationPermissionGranted by remember { mutableStateOf(context.isLocationPermissionsEnabled()) }
        var showSettingsDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var shouldGetBackgroundImageUri by remember { mutableStateOf(false) }
        var backgroundImageUri by remember { mutableStateOf<Uri?>(null) }
        var backgroundImageDate by remember { mutableStateOf<String?>(null) }

        val currentLocation by homeViewModel.currentLocationState.collectAsState(null)
        val isLoading by homeViewModel.isLoadingState.collectAsState()
        val errorMessage by homeViewModel.errorMessageState.collectAsState()
        val currentWeather by homeViewModel.currentWeatherState.collectAsState()
        val hourlyWeatherList by homeViewModel.hourlyWeatherListState.collectAsState()
        val tempType by homeViewModel.tempSymbol.collectAsState()
        val useDefaultBackground by homeViewModel.useDefaultBackground.collectAsState()
        val isLocationEnabled by homeViewModel.locationAvailabilityState.collectAsState()

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                shouldGetBackgroundImageUri = success
            }
        )

        LaunchedEffect(locationPermissionGranted, isLocationEnabled) {
            if (locationPermissionGranted) {
                homeViewModel.getUserLocation()
            }
        }

        LaunchedEffect(currentLocation) {
            currentLocation?.let {
                homeViewModel.getCurrentWeather(it.lat, it.lang)
            }
        }

        LaunchedEffect(useDefaultBackground) {
            if (useDefaultBackground) {
                backgroundImageUri = null
            } else {
                coroutineScope.launch {
                    val uri = getAllImagesUris(context).firstOrNull()
                    val date = getFileDate(context, uri)
                    backgroundImageUri = uri
                    if (date != null) {
                        backgroundImageDate = formatDate(date)
                    }
                }
            }
        }

        LaunchedEffect(shouldGetBackgroundImageUri) {
            if (shouldGetBackgroundImageUri) {
                coroutineScope.launch {
                    val uriList = getAllImagesUris(context)
                    backgroundImageUri = uriList.firstOrNull()
                }
                shouldGetBackgroundImageUri = false
                homeViewModel.saveUseDefaultBackground(false)
            }
        }

        LaunchedEffect(errorMessage) {
            if (errorMessage.isNullOrBlank().not()) showErrorDialog = true
        }

        if (shouldRequestLocationPermission) {
            LocationPermissionRequester(
                onPermissionGranted = { locationPermissionGranted = true },
                onPermissionDenied = { locationPermissionGranted = false }
            )
        }

        HomeScreenContent(
            isLoading = isLoading,
            isLocationEnable = isLocationEnabled ?: false,
            backgroundImageUri = backgroundImageUri,
            backgroundImageDate = backgroundImageDate,
            weatherData = currentWeather,
            hourlyWeatherList = hourlyWeatherList,
            lottieCompositionSpec = getWeatherLottieRes(currentWeather),
            onSettingClick = { showSettingsDialog = true },
            onEnableLocationClick = { context.requestEnableLocation() },
            onRetry = {
                if (currentLocation != null)
                    homeViewModel.getCurrentWeather(currentLocation!!.lat, currentLocation!!.lang)
            },
        )

        SettingsDialog(
            visible = showSettingsDialog,
            tempTypeName = if (tempType == TempType.CELSIUS) "Fahrenheit" else "Celsius",
            onDismiss = { showSettingsDialog = false },
            onTakePicClick = {
                val photoUri = createImageUri(context)
                cameraLauncher.launch(photoUri)
            },
            onRestBackground = { homeViewModel.saveUseDefaultBackground(true) },
            onChangeTempTypeClick = { homeViewModel.saveTempType(if (tempType == TempType.CELSIUS) TempType.FAHRENHEIT else TempType.CELSIUS) },
        )

        ErrorDialog(
            visible = showErrorDialog,
            message = errorMessage ?: "",
            onDismiss = { showErrorDialog = false },
            onRetry = {
                if (currentLocation != null)
                    homeViewModel.getCurrentWeather(currentLocation!!.lat, currentLocation!!.lang)
            }
        )
    }
}