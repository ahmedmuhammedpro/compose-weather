package com.ahmedpro.weathercompose.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.ahmedpro.domain.model.LatLang
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface LocationUpdateCallback {
    fun onLocationReceived(latLang: LatLang)
    fun onLocationAvailabilityChange(isAvailable: Boolean)
}

@SuppressLint("MissingPermission")
class UserLocation @Inject constructor(@ApplicationContext context: Context) {
    private val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback

    suspend fun getUserLocation(locationUpdateCallback: LocationUpdateCallback) =
        withContext(Dispatchers.IO) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    for (location in result.locations) {
                        locationUpdateCallback.onLocationReceived(
                            LatLang(
                                location.latitude,
                                location.longitude
                            )
                        )
                        Log.d("LOCATION_TAG", "${location.latitude},${location.longitude}")
                    }

                    locationProvider.lastLocation
                        .addOnSuccessListener { location ->
                            location?.let {
                                locationUpdateCallback.onLocationReceived(
                                    (LatLang(
                                        location.latitude,
                                        location.longitude
                                    ))
                                )
                            }
                        }
                        .addOnFailureListener {
                            Log.e("LOCATION_TAG", "${it.message}")
                        }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    locationUpdateCallback.onLocationAvailabilityChange(locationAvailability.isLocationAvailable)
                }
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                TimeUnit.SECONDS.toMillis(60),
            ).build()

            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    fun stopLocationUpdate() {
        try {
            if (this::locationCallback.isInitialized) {
                locationProvider.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("LOCATION_TAG", "Location Callback removed.")
                        } else {
                            Log.d("LOCATION_TAG", "Failed to remove Location Callback.")
                        }
                    }
            }
        } catch (se: SecurityException) {
            Log.e("LOCATION_TAG", "Failed to remove Location Callback.. $se")
        }
    }
}