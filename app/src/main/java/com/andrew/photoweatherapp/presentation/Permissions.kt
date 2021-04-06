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
const val CAMERA_REQUEST_CODE = 101
const val LOCATION_REQUEST_CODE = 102

fun Context.isPermissionGranted(permission: String) = ContextCompat
    .checkSelfPermission(this, permission)
    .let { it == PackageManager.PERMISSION_GRANTED }

fun Fragment.onDeniedPermission(message: Int, permission: String,requestPermission: () -> Unit) = AlertDialog
    .Builder(requireContext())
    .setMessage(message)
    .setPositiveButton(R.string.allow) { _, _ -> onAllowedRetry(permission,requestPermission) }
    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
    .create()
    .show()

private fun Fragment.onAllowedRetry(permission: String, requestPermission: () -> Unit) =
    if (shouldShowRequestPermissionRationale(permission)) {
        requestPermission()
    } else {
        requireContext().openSettings()
    }

fun Context.openSettings() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    .apply { data = Uri.fromParts("package", packageName, null) }
    .let { startActivity(it) }

