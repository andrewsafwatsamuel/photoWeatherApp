package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andrew.photoweatherapp.databinding.FragmentCameraBinding
import com.andrew.photoweatherapp.presentation.*

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
                image.image?.apply { file.save(toByteArray());viewModel.setCaptured(file.path) }
                image.close()

            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("#########", exception.message, exception)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CameraViewModel::class.java]
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
        binding.captureImageView.setOnClickListener { capture() }
        binding.cancelImageView.setOnClickListener { viewModel.cancelPhoto() }
        binding.saveImageView.setOnClickListener { viewModel.setSaved() }
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::drawStates)
    }

    private fun capture() = viewModel.setLoading().let {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            imageCaptureCallbacks
        )
        Log.d("######3", "capture")
    }

    private fun startCamera() {
        cameraProvider.addListener({
            preview.setSurfaceProvider(binding.cameraView.surfaceProvider)
            camera = cameraProvider.get()
                .bindToLifecycle(viewLifecycleOwner, cameraSelector.build(), preview, imageCapture)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun drawStates(state: CameraState) = when (state) {
        is IdleState -> drawIdle()
        is LoadingState -> disableButtons()
        is CapturedState -> drawCaptured()
        is com.andrew.photoweatherapp.presentation.features.cameraScreen.SavedState -> drawSaved()
    }

    private fun drawIdle() = with(binding) {
        cameraView.show()
        captureImageView.show()
        saveLayout.hide()
        photoViewImageView.hide()
        enableButtons()

    }

    private fun drawCaptured() = with(binding) {
        cameraView.hide()
        captureImageView.hide()
        saveLayout.show()
        photoViewImageView.show()
        photoViewImageView.setImageURI(Uri.parse(viewModel.temporaryUriString))
        enableButtons()
    }

    private fun enableButtons() = with(binding) {
        captureImageView.enable()
        saveImageView.enable()
        cancelImageView.enable()
    }

    private fun disableButtons() = with(binding) {
        captureImageView.disable()
        saveImageView.disable()
        cancelImageView.disable()
    }

    private fun drawSaved() {
        Toast.makeText(requireContext(), "saved", Toast.LENGTH_LONG).show()
        enableButtons()
    }
}