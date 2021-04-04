package com.andrew.photoweatherapp.presentation

import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.lang.Exception

private val locationProviderClient by lazy {
    LocationServices.getFusedLocationProviderClient(appContext)
}

private val cancellationToken by lazy {
    object : CancellationToken() {
        override fun isCancellationRequested() = false
        override fun onCanceledRequested(p0: OnTokenCanceledListener) = this
    }
}

fun Fragment.requestLocationOnGrantedPermission(
    onSuccess: (Location?) -> Unit,
    onError: (Exception) -> Unit,
    locationProvider: FusedLocationProviderClient = locationProviderClient
) = if (context?.isPermissionGranted(LOCATION_PERMISSION) == true) {
    locationProvider.getCurrentLocation(onSuccess, onError)
} else {
    requestLocationPermission()
}

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.getCurrentLocation(
    onSuccess: (Location?) -> Unit,
    onError: (Exception) -> Unit
) {
    getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, cancellationToken)
        .addOnSuccessListener(onSuccess)
        .addOnFailureListener(onError)
}

fun Fragment.requestLocationPermission() =
    requestPermissions(arrayOf(LOCATION_PERMISSION), LOCATION_REQUEST_CODE)

fun Fragment.onPermissionResult(
    onSuccess: (Location?) -> Unit,
    onError: (Exception) -> Unit,
    locationProvider: FusedLocationProviderClient = locationProviderClient
) = if (context?.isPermissionGranted(LOCATION_PERMISSION) == true) {
    requestLocationOnGrantedPermission(onSuccess, onError, locationProvider)
} else {
    onDeniedPermission(R.string.grant_location_permission,LOCATION_PERMISSION)
}
