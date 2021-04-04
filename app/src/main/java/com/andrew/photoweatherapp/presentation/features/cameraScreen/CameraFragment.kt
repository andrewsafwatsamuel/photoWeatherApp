package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.R

class CameraFragment:Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    )=inflater.inflate(R.layout.fragment_camera,container,false)

}