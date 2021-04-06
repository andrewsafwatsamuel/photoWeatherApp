package com.andrew.photoweatherapp.domain

import com.andrew.photoweatherapp.domain.dataSources.apiClient
import com.andrew.photoweatherapp.entities.WeatherData
import retrofit2.Response

interface WeatherDataRepository {
    suspend fun retrieveByCityName(cityName: String): Response<WeatherData>
}

val weatherDataRepository by lazy { DefaultWeatherDataRepository() }

class DefaultWeatherDataRepository : WeatherDataRepository {
    override suspend fun retrieveByCityName(cityName: String) =
        apiClient.getWeatherDataByCityName(cityName)

}