package com.andrew.photoweatherapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
const val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSIONS_CODE = 101

fun Fragment.requestNotGrantedPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(
        arrayOf(CAMERA_PERMISSION, LOCATION_PERMISSION),
        PERMISSIONS_CODE
    )
}

fun Context.getNotGrantedPermissions(
    notGrantedPermissions: MutableList<String> = mutableListOf()
) = listOf(CAMERA_PERMISSION, LOCATION_PERMISSION)
    .forEach { if (!isPermissionGranted(it)) notGrantedPermissions.add(it) }
    .let { notGrantedPermissions }

fun Context.isPermissionGranted(permission: String) = ContextCompat
    .checkSelfPermission(this, permission)
    .let { it == PackageManager.PERMISSION_GRANTED }
