package com.andrew.photoweatherapp.domain.dataSources

import android.os.Build
import com.andrew.photoweatherapp.entities.WeatherData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val BASE_URL_21 = "http://api.openweathermap.org/data/2.5/"
private const val APP_ID = "86a9abf0018f9fe698c39241d29ee0ab"

private fun request(chain: Interceptor.Chain): okhttp3.Response = with(chain.request()) {
    url().newBuilder()
        .addQueryParameter("appid", APP_ID)
        .addQueryParameter("units", "metric")
        .build()
        .let { url -> newBuilder().url(url).build() }
        .let { request -> chain.proceed(request) }
}

private val httpClient by lazy {
    OkHttpClient().newBuilder()
        .addInterceptor { request(it) }
        .build()
}

private val retrofitInstance by lazy {
    Retrofit.Builder()
        .baseUrl(setBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
}

private fun setBaseUrl() =
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) BASE_URL_21 else BASE_URL

val apiClient: ApiClient by lazy { retrofitInstance.create(ApiClient::class.java) }

interface ApiClient {

    @GET("weather")
    suspend fun getWeatherDataByCityName(
        @Query("q") cityName: String
    ): Response<WeatherData>

}