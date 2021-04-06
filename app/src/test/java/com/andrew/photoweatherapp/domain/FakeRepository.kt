package com.andrew.photoweatherapp.domain

import com.andrew.photoweatherapp.entities.WeatherData
import okhttp3.ResponseBody
import retrofit2.Response

class FakeRepository:WeatherDataRepository {
    override suspend fun retrieveByCityName(cityName: String): Response<WeatherData> =
        Response.success(WeatherData())
}

class FakeRepositoryErrorResponse:WeatherDataRepository {
    override suspend fun retrieveByCityName(cityName: String): Response<WeatherData> =
        Response.error(404, ResponseBody.create(null,"not found"))
}