package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andrew.photoweatherapp.databinding.FragmentCameraBinding
import com.andrew.photoweatherapp.presentation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraFragment : Fragment() {

    lateinit var binding: FragmentCameraBinding
        private set
    private val cameraProvider by lazy { ProcessCameraProvider.getInstance(requireContext()) }
    private lateinit var camera: Camera
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
                image.image?.apply { file.save(toByteArray());viewModel.setCaptured(file.path) }
                image.close()
            }
        }
    }

    val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CameraViewModel::class.java]
    }

    private val args by lazy { CameraFragmentArgs.fromBundle(requireArguments()) }

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
        setButtonClicks()
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::drawStates)
        drawWeatherData()
    }

    private fun setButtonClicks() = with(binding) {
        captureImageView.setOnClickListener { capture() }
        cancelImageView.setOnClickListener { viewModel.cancelPhoto() }
        saveImageView.setOnClickListener { viewModel.setLoading();savePhoto() }
    }

    private fun drawWeatherData() = with(binding) {
        cityNameTextView.text = args.data.name.toString()
        currentTempTextView.text = args.data.main?.temp.toString()
        feelsLikeTextView.text = args.data.main?.feelsLike.toString()
        humidityTextView.text = args.data.main?.humidity.toString()
        windTextView.text = args.data.wind?.speed?.toString()
        pressureTextView.text = args.data.main?.pressure.toString()
    }

    private fun capture() {
        viewModel.setLoading()
        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()),imageCaptureCallbacks)
    }

    private fun startCamera() {
        cameraProvider.addListener({
            preview.setSurfaceProvider(binding.cameraView.surfaceProvider)
            camera = cameraProvider.get()
                .bindToLifecycle(viewLifecycleOwner, cameraSelector.build(), preview, imageCapture)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun savePhoto() = CoroutineScope(viewModel.cameraJob + Dispatchers.Main).launch {
        val uri = requireContext().saveCapturedPhoto(binding.frameLayout)
        viewModel.setSaved(uri)
    }
}