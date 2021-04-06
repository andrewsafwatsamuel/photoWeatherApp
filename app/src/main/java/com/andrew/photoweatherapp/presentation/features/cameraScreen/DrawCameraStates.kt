package com.andrew.photoweatherapp.presentation.features.cameraScreen

import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.andrew.photoweatherapp.presentation.disable
import com.andrew.photoweatherapp.presentation.enable
import com.andrew.photoweatherapp.presentation.hide
import com.andrew.photoweatherapp.presentation.show

fun CameraFragment.drawStates(state: CameraState) = when (state) {
    is IdleState -> drawIdle()
    is LoadingState -> disableButtons()
    is CapturedState -> drawCaptured()
    is Saved -> navigateToShare(state.uri?:"")
}
private fun CameraFragment.navigateToShare(uri: String) = CameraFragmentDirections
    .actionCameraFragmentToShareFragment(uri)
    .let { findNavController().navigate(it) }
private fun CameraFragment.drawIdle() = with(binding) {
    cameraView.show()
    captureImageView.show()
    saveLayout.hide()
    photoViewImageView.hide()
    enableButtons()
}

private fun CameraFragment.drawCaptured() = with(binding) {
    cameraView.hide()
    captureImageView.hide()
    saveLayout.show()
    photoViewImageView.show()
    photoViewImageView.setImageURI(Uri.parse(viewModel.temporaryUriString))
    enableButtons()
}

private fun CameraFragment.enableButtons() = with(binding) {
    captureImageView.enable()
    saveImageView.enable()
    cancelImageView.enable()
}

private fun CameraFragment.disableButtons() = with(binding) {
    captureImageView.disable()
    saveImageView.disable()
    cancelImageView.disable()
}
