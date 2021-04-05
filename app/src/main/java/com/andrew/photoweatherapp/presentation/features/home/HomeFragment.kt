package com.andrew.photoweatherapp.presentation.features.home

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.databinding.FragmentHomeBinding
import com.andrew.photoweatherapp.presentation.*
import java.lang.Exception

class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding
        private set

    private val viewModel by lazy {
        ViewModelProvider(this@HomeFragment, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchCardView.setOnClickListener {
            viewModel.getDataByCityName(getCityText(),true)
        }
        viewModel.state.observe(viewLifecycleOwner,::drawStates)
        binding.savedPhotosButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_historyFragment) }
    }

    private fun getCityText()=binding.cityNameEditTextText.text?.toString()?:""

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

    private fun onLocationError(exception: Exception) = Unit

    private fun onLocationSuccess(location: Location?) = Unit
}
