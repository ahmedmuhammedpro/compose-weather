package com.ahmedpro.domain.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ahmedpro.domain.model.WeatherData
import com.ahmedpro.domain.utils.TempType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

internal val Context.dataStore by preferencesDataStore("weather-datastore")

interface PrefStore {
    val tempType: Flow<TempType>
    val cachedWeatherData: Flow<WeatherData>
    val useDefaultBackground: Flow<Boolean>

    suspend fun saveTempType(type: TempType)
    suspend fun saveWeatherData(weatherData: WeatherData)
    suspend fun saveUsingDefaultBackground(value: Boolean)
}

@Singleton
class PrefStoreImpl @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context
) : PrefStore {
    private val dataStore = context.dataStore

    override val tempType: Flow<TempType> = dataStore.data.map { prefs ->
        val type = prefs[KEY_PREF_TEMP_TYPE]
        if (type == TempType.CELSIUS.name) TempType.CELSIUS
        else TempType.FAHRENHEIT
    }
    override val cachedWeatherData: Flow<WeatherData> = dataStore.data.map { prefs ->
        gson.fromJson(prefs[KEY_PREF_CACHED_WEATHER_DATA], weatherDataType)
    }
    override val useDefaultBackground: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_USE_DEFAULT_BACKGROUND] ?: true
    }

    override suspend fun saveTempType(type: TempType) {
        dataStore.edit { prefs -> prefs[KEY_PREF_TEMP_TYPE] = type.name }
    }

    override suspend fun saveWeatherData(weatherData: WeatherData) {
        dataStore.edit { prefs -> prefs[KEY_PREF_CACHED_WEATHER_DATA] = gson.toJson(weatherData, weatherDataType) }
    }

    override suspend fun saveUsingDefaultBackground(value: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_USE_DEFAULT_BACKGROUND] = value }
    }

    companion object {
        val KEY_PREF_TEMP_TYPE = stringPreferencesKey("key_temp_type")
        val KEY_USE_DEFAULT_BACKGROUND = booleanPreferencesKey("key_use_default_background")
        val KEY_PREF_CACHED_WEATHER_DATA = stringPreferencesKey("key_cached_weather_data")

        val weatherDataType: Type = object : TypeToken<WeatherData>() {}.type
    }

}