package com.andrew.photoweatherapp.presentation.features.home

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.presentation.*
import java.lang.Exception

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    private fun onLocationSuccess(location: Location?) =Unit
    private fun onLocationError(exception: Exception) = Unit

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.size == 1 && requestCode == LOCATION_REQUEST_CODE) onPermissionResult(
            ::onLocationSuccess,
            ::onLocationError
        )
    }
}
