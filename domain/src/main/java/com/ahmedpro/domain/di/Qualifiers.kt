package com.ahmedpro.domain.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class WeatherServiceClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class WeatherServiceRetrofit
