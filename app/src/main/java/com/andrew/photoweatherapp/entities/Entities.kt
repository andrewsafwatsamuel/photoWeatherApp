package com.andrew.photoweatherapp.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherData(
    val weather: List<Weather>? = listOf(),
    val main: Main? = Main(),
    val wind: Wind? = Wind(),
    @field:SerializedName("sys")
    val sunState: SunState? = SunState(),
    val name: String? = ""
) : Serializable

data class Main(
    val temp: Double? = 0.0,
    @field:SerializedName("feels_like")
    val feelsLike: Double? = 0.0,
    @field:SerializedName("temp_min")
    val tempMin: Double? = 0.0,
    @field:SerializedName("temp_max")
    val tempMax: Double? = 0.0,
    val pressure: Long? = 0L,
    val humidity: Long? = 0L
) : Serializable

data class SunState(
    val sunrise: Long? = 0L,
    val sunset: Long? = 0L
) : Serializable

data class Weather(
    val id: Long? = 0L,
    val main: String? = "",
    val description: String? = "",
    val icon: String? = ""
) : Serializable

data class Wind(
    val speed: Double? = 0.0,
    val deg: Long? = 0L
) : Serializable
