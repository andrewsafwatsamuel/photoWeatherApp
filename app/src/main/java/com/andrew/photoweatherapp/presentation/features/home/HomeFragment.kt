package com.andrew.photoweatherapp.presentation.features.home

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.domain.GetWeatherDataCase
import com.andrew.photoweatherapp.domain.WeatherDataState
import com.andrew.photoweatherapp.entities.WeatherData
import com.andrew.photoweatherapp.presentation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationOnGrantedPermission(::onLocationSuccess, ::onLocationError)
    }

    private fun onLocationSuccess(location: Location?) {
        val useCase = GetWeatherDataCase()
        val state = MutableLiveData<WeatherDataState>()
        CoroutineScope(Dispatchers.Main).launch {
            useCase(location?.longitude, location?.latitude, state, true)
        }
        state.observe(viewLifecycleOwner) {
            Log.d("############", it.toString())
        }
    }

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
