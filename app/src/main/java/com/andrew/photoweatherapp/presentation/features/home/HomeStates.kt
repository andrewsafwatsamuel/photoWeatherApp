package com.andrew.photoweatherapp.presentation.features.home

import androidx.navigation.fragment.findNavController
import com.andrew.photoweatherapp.databinding.FragmentHomeBinding
import com.andrew.photoweatherapp.domain.*
import com.andrew.photoweatherapp.entities.WeatherData
import com.andrew.photoweatherapp.presentation.disable
import com.andrew.photoweatherapp.presentation.enable
import com.andrew.photoweatherapp.presentation.hide
import com.andrew.photoweatherapp.presentation.show

fun HomeFragment.drawStates(state: WeatherDataState) = when (state) {
    is IdleState -> drawIdle()
    is LoadingState -> drawLoading()
    is ErrorState -> drawError(state.message)
    is DataState -> drawData(state.data)
    is BadLocationState -> drawBadLocation()
}

private fun HomeFragment.drawIdle() = with(binding) {
    searchCardView.enable()
    weatherDataProgressBar.hide()
    idleLayout.show()
    dataLayout.hide()
}

private fun HomeFragment.drawLoading() = with(binding) {
    searchCardView.disable()
    weatherDataProgressBar.show()
    idleLayout.hide()
    dataLayout.hide()
}

private fun HomeFragment.drawError(message: String?) = with(binding) {
    searchCardView.enable()
}

private fun HomeFragment.drawData(data: WeatherData) = with(binding) {
    searchCardView.enable()
    weatherDataProgressBar.hide()
    idleLayout.hide()
    dataLayout.show()
    drawDataState(data)
}

private fun HomeFragment.drawDataState(data: WeatherData)= with(binding) {
    cityNameTextView.text = data.name
    stateTextView.text = (data.wind?.speed ?: 0).toString()
    currentTempTextView.text = data.main?.temp.toString()
    feelsLikeTextView.text = data.main?.feelsLike.toString()
    pressureTextView.text = data.main?.pressure.toString()
    humidityTextView.text = data.main?.humidity.toString()
    windTextView.text = (data.wind?.speed ?: 0).toString()
    cameraCardView.setOnClickListener {
        HomeFragmentDirections.actionHomeFragmentToCameraFragment(data)
            .let { findNavController().navigate(it) }
    }

}

private fun HomeFragment.drawBadLocation() = with(binding) {
    searchCardView.enable()
}