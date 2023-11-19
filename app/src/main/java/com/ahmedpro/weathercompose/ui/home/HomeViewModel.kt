package com.ahmedpro.weathercompose.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedpro.domain.base.Result
import com.ahmedpro.domain.model.LatLang
import com.ahmedpro.domain.model.Main
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.usecase.WeatherUseCases
import com.ahmedpro.domain.utils.TempType
import com.ahmedpro.domain.utils.convertFromCelsiusToFahrenheit
import com.ahmedpro.domain.utils.convertFromFahrenheitToCelsius
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

    private val _currentWeatherState = MutableStateFlow<WeatherData?>(null)
    val currentWeatherState: StateFlow<WeatherData?> = _currentWeatherState

    init {
        viewModelScope.launch {
            _tempType.value = getTempTypeUseCase()
            _useDefaultBackground.value = getUseDefaultBackgroundUseCase()
            _currentWeatherState.value = getCachedWeatherUseCase()
        }
    }

    fun getUserLocation() {
        viewModelScope.launch {
            userLocation.getUserLocation {
                _currentLocationState.value = it
            }
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