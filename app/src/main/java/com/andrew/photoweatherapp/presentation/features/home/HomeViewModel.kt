package com.andrew.photoweatherapp.presentation.features.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andrew.photoweatherapp.domain.GetWeatherDataUseCase
import com.andrew.photoweatherapp.domain.WeatherDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: GetWeatherDataUseCase = GetWeatherDataUseCase(),
    val state: MutableLiveData<WeatherDataState> = MutableLiveData()
) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    fun getDataByLocation(
        longitude: Double?,
        latitude: Double?,
        isConnected: Boolean,
        scope: CoroutineScope = uiScope
    ) = scope.launch { useCase(longitude, latitude, state, isConnected) }

    fun getDataByCityName(
        cityName: String,
        isConnected: Boolean,
        scope: CoroutineScope = uiScope
    ) = scope.launch { useCase(cityName, state, isConnected) }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}