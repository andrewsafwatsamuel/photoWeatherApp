package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.databinding.FragmentCameraBinding

class CameraFragment:Fragment() {

    private lateinit var binding : FragmentCameraBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

}