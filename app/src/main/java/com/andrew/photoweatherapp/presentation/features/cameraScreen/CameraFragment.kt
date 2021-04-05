package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.annotation.SuppressLint
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.databinding.FragmentCameraBinding
import com.andrew.photoweatherapp.presentation.createTempFile
import com.andrew.photoweatherapp.presentation.save
import com.andrew.photoweatherapp.presentation.toByteArray
import java.io.*
import java.lang.Exception
import java.nio.ByteBuffer

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val cameraProvider by lazy { ProcessCameraProvider.getInstance(requireContext()) }
    lateinit var camera: Camera
    private val preview by lazy { Preview.Builder().build() }
    private val imageCapture by lazy { ImageCapture.Builder().build() }
    private val cameraSelector by lazy {
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
    }

    private val imageCaptureCallbacks by lazy {
        object : ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val file = requireContext().createTempFile()
                image.image?.run { file.save(toByteArray()) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        binding.cameraView.setOnClickListener { capture() }
    }

    private fun capture() = imageCapture.takePicture(
        ContextCompat.getMainExecutor(requireContext()),
        imageCaptureCallbacks
    )

    private fun startCamera() {
        cameraProvider.addListener(Runnable {
            preview.setSurfaceProvider(binding.cameraView.surfaceProvider)
            camera = cameraProvider.get()
                .bindToLifecycle(viewLifecycleOwner, cameraSelector.build(), preview, imageCapture)
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}