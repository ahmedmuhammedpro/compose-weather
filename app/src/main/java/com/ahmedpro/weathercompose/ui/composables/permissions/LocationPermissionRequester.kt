package com.ahmedpro.weathercompose.ui.composables.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat

@Composable
fun LocationPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    requestPreciseLocation: Boolean = true
) {
    val permissionRequester = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.containsValue(false)) {
            onPermissionDenied.invoke()
        } else {
            onPermissionGranted.invoke()
        }
    }

    SideEffect {
        // Prepare permissions list
        val permissions = buildList {
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (requestPreciseLocation) add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        permissionRequester.launch(
            permissions.toTypedArray()
        )
    }
}

fun Context.isLocationPermissionsEnabled(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}