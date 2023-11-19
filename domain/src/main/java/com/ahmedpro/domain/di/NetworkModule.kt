package com.ahmedpro.domain.di

import com.ahmedpro.domain.remote.service.WEATHER_API_BASE_URL
import com.ahmedpro.domain.remote.service.WeatherService
import com.ahmedpro.domain.remote.service.WeatherServiceInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesGson(): Gson = GsonBuilder()
        .serializeNulls()
        .create()

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    @WeatherServiceClient
    fun providesWeatherServiceClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ) = OkHttpClient.Builder()
        .addInterceptor(WeatherServiceInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()


    @Provides
    @Singleton
    @WeatherServiceRetrofit
    fun providesWeatherServiceRetrofit(
        gson: Gson,
        @WeatherServiceClient client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun providesWeatherService(@WeatherServiceRetrofit retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)
}
