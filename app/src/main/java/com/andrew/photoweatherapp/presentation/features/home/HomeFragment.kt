package com.andrew.photoweatherapp.presentation.features.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.databinding.FragmentHomeBinding
import com.andrew.photoweatherapp.entities.WeatherData
import com.andrew.photoweatherapp.presentation.*
import java.lang.Exception

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
        private set

    private val viewModel by lazy {
        ViewModelProvider(
            this@HomeFragment,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchCardView.setOnClickListener {
            context?.run { viewModel.getDataByCityName(getCityText(), checkConnectivity()) }
        }
        viewModel.state.observe(viewLifecycleOwner, ::drawStates)
        binding.savedPhotosButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_historyFragment) }

    }

    private fun getCityText() = binding.cityNameEditTextText.text?.toString() ?: ""

    @RequiresApi(Build.VERSION_CODES.M)
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
        if (permissions.size == 1 && requestCode == CAMERA_REQUEST_CODE) onCameraPermissionResult()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onCameraPermissionResult() = weatherData?.let {
        if (context?.checkSelfPermission(CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) navigateToCamera(it)
        else onDeniedPermission(R.string.grant_camera_permission, CAMERA_PERMISSION){checkCameraPermission(it)}
    }

    private fun onLocationError(exception: Exception) = Unit

    private fun onLocationSuccess(location: Location?) = Unit

    fun checkCameraPermission(data: WeatherData) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) shouldRequestPermission(data)
        else navigateToCamera(data)

    private fun shouldRequestPermission(data: WeatherData) =
        if (requireContext().isPermissionGranted(CAMERA_PERMISSION)) navigateToCamera(data)
        else requestPermissions(arrayOf(CAMERA_PERMISSION), CAMERA_REQUEST_CODE)

    private fun navigateToCamera(data: WeatherData) = HomeFragmentDirections
        .actionHomeFragmentToCameraFragment(data)
        .let { findNavController().navigate(it) }

}
