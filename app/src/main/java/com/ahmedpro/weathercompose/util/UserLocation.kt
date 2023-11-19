package com.ahmedpro.weathercompose.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import com.ahmedpro.domain.model.LatLang
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("MissingPermission")
class UserLocation @Inject constructor(@ApplicationContext context: Context) {
    private val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback

    suspend fun getUserLocation(onLocationReceived: (LatLang) -> Unit) =
        withContext(Dispatchers.IO) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    for (location in result.locations) {
                        onLocationReceived(LatLang(location.latitude, location.longitude))
                        Log.d("LOCATION_TAG", "${location.latitude},${location.longitude}")
                    }

                    locationProvider.lastLocation
                        .addOnSuccessListener { location ->
                            location?.let {
                                onLocationReceived(LatLang(location.latitude, location.longitude))
                            }
                        }
                        .addOnFailureListener {
                            Log.e("LOCATION_TAG", "${it.message}")
                        }
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

interface LocationSettingsListener {
    fun onLocationSettingsChanged(locationSettingsStates: LocationSettingsStates)
}

fun observeLocationSettings(
    context: Context,
    activity: Activity,
    requestCode: Int,
    listener: LocationSettingsListener?
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        TimeUnit.SECONDS.toMillis(60),
    ).build()

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)

    val task: Task<LocationSettingsResponse> =
        LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())

    task.addOnCompleteListener { result ->
        try {
            val response = result.getResult(ApiException::class.java)
            val locationSettingsStates = response.locationSettingsStates
            listener?.onLocationSettingsChanged(locationSettingsStates!!)
        } catch (exception: ApiException) {
            if (exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    val resolvable = exception as ResolvableApiException
                    resolvable.startResolutionForResult(activity, requestCode)
                } catch (e: IntentSender.SendIntentException) {
                    // Handle the exception
                }
            }
        }
    }
}