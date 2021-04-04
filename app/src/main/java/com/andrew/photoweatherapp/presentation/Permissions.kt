package com.andrew.photoweatherapp.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.R

const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
const val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSIONS_CODE = 101
const val LOCATION_REQUEST_CODE = 102

fun Fragment.requestNotGrantedPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(
        arrayOf(CAMERA_PERMISSION, LOCATION_PERMISSION),
        PERMISSIONS_CODE
    )
}

fun Context.isPermissionGranted(permission: String) = ContextCompat
    .checkSelfPermission(this, permission)
    .let { it == PackageManager.PERMISSION_GRANTED }

fun Fragment.onDeniedPermission(message: Int, permission: String) = AlertDialog
    .Builder(requireContext())
    .setMessage(message)
    .setPositiveButton(R.string.allow) { _, _ -> onAllowedRetry(permission) }
    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
    .create()
    .show()

private fun Fragment.onAllowedRetry(permission: String) =
    if (shouldShowRequestPermissionRationale(permission)) requestLocationPermission()
    else requireContext().openSettings()

private fun Context.openSettings() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    .apply { data = Uri.fromParts("package", packageName, null) }
    .let { startActivity(it) }

