package com.andrew.photoweatherapp.domain

import androidx.lifecycle.MutableLiveData
import com.andrew.photoweatherapp.entities.WeatherData
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

sealed class WeatherDataState
object IdleState : WeatherDataState()
object LoadingState : WeatherDataState()
object BadLocationState : WeatherDataState()
data class ErrorState(val message: String?) : WeatherDataState()
data class DataState(val data: WeatherData) : WeatherDataState()

class GetWeatherDataCase(
    private val repository: WeatherDataRepository = weatherDataRepository
) {
    suspend operator fun invoke(
        cityName: String,
        state: MutableLiveData<WeatherDataState>,
        isConnected: Boolean,
        context: CoroutineContext=Dispatchers.IO
    ) = context.takeIf { cityName.isNotBlank() && isConnected }
        ?.takeUnless { state.value == LoadingState }
        ?.also { state.postValue(LoadingState) }
        ?.let { makeRequest(cityName, state,context) }

    private suspend fun makeRequest(
        cityName: String,
        state: MutableLiveData<WeatherDataState>,
        context: CoroutineContext
    ) = try {
        withContext(context) { repository.retrieveByCityName(cityName) }
            .run { state.value = doOnSuccessfulResponse() }
    } catch (exception: Exception) {
        state.postValue(ErrorState(exception.message))
    }

    suspend operator fun invoke(
        longitude: Double?,
        latitude:Double?,
        state: MutableLiveData<WeatherDataState>,
        isConnected: Boolean,
        context: CoroutineContext=Dispatchers.IO
    ) = context.takeIf { isConnected }
        ?.takeUnless { state.value == LoadingState || longitude==null||latitude==null }
        ?.also { state.postValue(LoadingState) }
        ?.let { makeRequest(longitude!!, latitude!!,state,context) }

    private suspend fun makeRequest(
        longitude: Double,
        latitude:Double,
        state: MutableLiveData<WeatherDataState>,
        context: CoroutineContext
    ) = try {
        withContext(context) { repository.retrieveByLocation(longitude,latitude) }
            .run { state.value = doOnSuccessfulResponse() }
    } catch (exception: Exception) {
        state.postValue(ErrorState(exception.message))
    }

    private fun Response<WeatherData>.doOnSuccessfulResponse() =
        if (isSuccessful && body() != null) DataState(body()!!) else ErrorState(message())
}