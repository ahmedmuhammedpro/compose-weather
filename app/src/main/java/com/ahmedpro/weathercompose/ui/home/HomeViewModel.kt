package com.ahmedpro.weathercompose.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedpro.domain.base.Result
import com.ahmedpro.domain.model.HourlyData
import com.ahmedpro.domain.model.HourlyWeatherList
import com.ahmedpro.domain.model.LatLang
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.usecase.WeatherUseCases
import com.ahmedpro.domain.utils.TempType
import com.ahmedpro.domain.utils.celsiusToFahrenheit
import com.ahmedpro.domain.utils.convertFromCelsiusToFahrenheit
import com.ahmedpro.domain.utils.convertFromFahrenheitToCelsius
import com.ahmedpro.domain.utils.fahrenheitToCelsius
import com.ahmedpro.weathercompose.util.LocationUpdateCallback
import com.ahmedpro.weathercompose.util.UserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userLocation: UserLocation,
    private val getCurrentWeatherUseCase: WeatherUseCases.GetCurrentWeatherUseCase,
    private val getHourlyWeatherListUseCase: WeatherUseCases.GetHourlyWeatherListUseCase,
    private val getCachedWeatherUseCase: WeatherUseCases.GetCachedWeatherUseCase,
    private val getTempTypeUseCase: WeatherUseCases.GetTempTypeUseCase,
    private val getUseDefaultBackgroundUseCase: WeatherUseCases.GetUseDefaultBackgroundUseCase,
    private val saveTempTypeUseCase: WeatherUseCases.SaveTempTypeUseCase,
    private val saveUseDefaultBackgroundUseCase: WeatherUseCases.SaveUseDefaultBackgroundUseCase,
) : ViewModel() {

    private val _currentLocationState = MutableStateFlow<LatLang?>(null)
    val currentLocationState: StateFlow<LatLang?> = _currentLocationState

    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState

    private val _errorMessageState = MutableStateFlow<String?>(null)
    val errorMessageState: StateFlow<String?> = _errorMessageState

    private val _tempType = MutableStateFlow(TempType.CELSIUS)
    val tempSymbol: StateFlow<TempType> = _tempType

    private val _useDefaultBackground = MutableStateFlow(true)
    val useDefaultBackground: StateFlow<Boolean> = _useDefaultBackground

    private val _locationAvailabilityState = MutableStateFlow<Boolean?>(null)
    val locationAvailabilityState: StateFlow<Boolean?> = _locationAvailabilityState

    private val _currentWeatherState = MutableStateFlow<WeatherData?>(null)
    val currentWeatherState: StateFlow<WeatherData?> = _currentWeatherState

    private val _hourlyWeatherListState = MutableStateFlow<HourlyWeatherList?>(null)
    val hourlyWeatherListState: StateFlow<HourlyWeatherList?> = _hourlyWeatherListState

    init {
        viewModelScope.launch {
            val cachedWeatherData = getCachedWeatherUseCase()
            _tempType.value = getTempTypeUseCase()
            _useDefaultBackground.value = getUseDefaultBackgroundUseCase()
            _currentWeatherState.value = cachedWeatherData
            _currentLocationState.value =
                if (cachedWeatherData != null) LatLang(cachedWeatherData.coord.lat, cachedWeatherData.coord.lon)
                else null
        }
    }

    fun getUserLocation() {
        viewModelScope.launch {
            userLocation.getUserLocation(object : LocationUpdateCallback {
                override fun onLocationReceived(latLang: LatLang) {
                    _currentLocationState.value = latLang
                }

                override fun onLocationAvailabilityChange(isAvailable: Boolean) {
                    _locationAvailabilityState.value = isAvailable
                }
            })
        }
    }

    fun getCurrentWeather(lat: Double, lang: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getCurrentWeatherUseCase(
                    WeatherUseCases.GetCurrentWeatherUseCase.Params(lat.toFloat(), lang.toFloat())
                ).collect {
                    when (it) {
                        Result.Loading -> _isLoadingState.value = true
                        is Result.Error -> {
                            _isLoadingState.value = false
                            _errorMessageState.value =
                                it.errorDetails.message ?: it.errorDetails.throwable?.message
                            Log.e("HomeViewModel", "Error", it.errorDetails.throwable)
                        }

                        is Result.Success -> {
                            _isLoadingState.value = false
                            _currentWeatherState.value = it.data
                        }
                    }
                }

                getHourlyWeatherListUseCase(
                    WeatherUseCases.GetHourlyWeatherListUseCase.Params(lat.toFloat(), lang.toFloat())
                ).collect {
                    if (it is Result.Success) {
                        _hourlyWeatherListState.value = it.data
                    }
                }

                delay(TimeUnit.MINUTES.toMillis(1))
            }
        }
    }

    fun saveTempType(tempType: TempType) {
        viewModelScope.launch {
            _tempType.value = tempType
            val main = _currentWeatherState.value?.main
            _currentWeatherState.value = _currentWeatherState.value?.copy(
                main =
                if (tempType == TempType.CELSIUS) main!!.convertFromFahrenheitToCelsius()
                else main!!.convertFromCelsiusToFahrenheit()
            )

            val hourlyHourlyDateList = mutableListOf<HourlyData>()
            _hourlyWeatherListState.value?.hourly?.forEach {
                val newItem = it.copy(
                    temp =
                    if (tempType == TempType.FAHRENHEIT) it.temp.celsiusToFahrenheit()
                    else it.temp.fahrenheitToCelsius()
                )
                hourlyHourlyDateList.add(newItem)
            }

            _hourlyWeatherListState.value = _hourlyWeatherListState.value?.copy(
                hourly = hourlyHourlyDateList
            )

            saveTempTypeUseCase(tempType)
        }
    }

    fun saveUseDefaultBackground(value: Boolean) {
        viewModelScope.launch {
            _useDefaultBackground.value = value
            saveUseDefaultBackgroundUseCase(value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        userLocation.stopLocationUpdate()
    }
}