package com.ahmedpro.weathercompose.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.GnssStatus
import android.location.GpsStatus
import android.location.LocationManager
import android.net.Uri
import android.os.Handler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
        val locationManager by remember {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            mutableStateOf(lm)
        }
        val gpsStatusListener  by remember {
            val gsl = object : GnssStatus.Callback() {
                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    val x = status.satelliteCount
                    val y = 0
                }
            }
            mutableStateOf(gsl)
        }

        val currentLocation by homeViewModel.currentLocationState.collectAsState(null)
        val isLoading by homeViewModel.isLoadingState.collectAsState()
        val errorMessage by homeViewModel.errorMessageState.collectAsState()
        val currentWeather by homeViewModel.currentWeatherState.collectAsState()
        val tempType by homeViewModel.tempSymbol.collectAsState()
        val useDefaultBackground by homeViewModel.useDefaultBackground.collectAsState()

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                shouldGetBackgroundImageUri = success
            }
        )

        LaunchedEffect(locationPermissionGranted) {
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
                    backgroundImageUri = uriList.lastOrNull()
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

        DisposableEffect(locationPermissionGranted) {
            if (locationPermissionGranted) {
                locationManager.registerGnssStatusCallback(gpsStatusListener, Handler(context.mainLooper))
            }
            onDispose {
                if (locationPermissionGranted) locationManager.unregisterGnssStatusCallback(gpsStatusListener)
            }
        }

        HomeScreenContent(
            isLoading = isLoading,
            isLocationEnable = locationPermissionGranted,
            backgroundImageUri = backgroundImageUri,
            backgroundImageDate = backgroundImageDate,
            weatherData = currentWeather,
            lottieCompositionSpec = getWeatherLottieRes(currentWeather),
            onSettingClick = { showSettingsDialog = true },
            onRetry = { homeViewModel.getCurrentWeather(currentLocation?.lat ?: 0.0, currentLocation?.lang ?: 0.0) },
        )

        SettingsDialog(
            visible = showSettingsDialog,
            tempTypeName = if (tempType == TempType.CELSIUS) "Fahrenheit" else "Celsius",
            onTakePicClick = {
                val photoUri = createImageUri(context)
                cameraLauncher.launch(photoUri)
            },
            onRestBackground = { homeViewModel.saveUseDefaultBackground(true) },
            onChangeTempTypeClick = { homeViewModel.saveTempType(if (tempType == TempType.CELSIUS) TempType.FAHRENHEIT else TempType.CELSIUS) },
            onDismiss = { showSettingsDialog = false },
        )

        ErrorDialog(
            visible = showErrorDialog,
            message = errorMessage ?: "",
            onRetry = { homeViewModel.getCurrentWeather(currentLocation?.lat ?: 0.0, currentLocation?.lang ?: 0.0) },
            onDismiss = { showErrorDialog = false }
        )
    }
}