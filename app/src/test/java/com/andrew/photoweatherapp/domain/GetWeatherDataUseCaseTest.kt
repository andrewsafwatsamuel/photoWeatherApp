package com.andrew.photoweatherapp.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetWeatherDataUseCaseTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    lateinit var useCase: GetWeatherDataUseCase

    private val stateLiveData = MutableLiveData<WeatherDataState>()

    @Before
    fun initialize() {
        useCase = GetWeatherDataUseCase(FakeRepository())
        stateLiveData.value = null
    }


    @Test
    fun `invoke with successful conditions retrieve data with dataState`() = runBlockingTest {

        //arrange
        val cityName = "cairo"
        val isConnected = true

        val context = TestCoroutineDispatcher()

        //act
        useCase(cityName, stateLiveData, isConnected, context)

        //assert
        assert(stateLiveData.value is DataState)
    }

    @Test
    fun `invoke with blank city name nothing will happen and satate will be null`() =
        runBlockingTest {
            //arrange
            val cityName = ""
            val isConnected = true

            val context = TestCoroutineDispatcher()

            //act
            useCase(cityName, stateLiveData, isConnected, context)

            //assert
            assert(stateLiveData.value == null)
        }

    @Test
    fun `invoke when not connected nothing will happen and satate will be null`() =
        runBlockingTest {

            //arrange
            val cityName = "cairo"
            val isConnected = false

            val context = TestCoroutineDispatcher()

            //act
            useCase(cityName, stateLiveData, isConnected, context)

            //assert
            assert(stateLiveData.value == null)
        }

    @Test
    fun `invoke when state is loading nothing will happen and satate will be loading`() =
        runBlockingTest {

            //arrange
            val cityName = "cairo"
            val isConnected = true
            val context = TestCoroutineDispatcher()
            stateLiveData.value = LoadingState

            //act
            useCase(cityName, stateLiveData, isConnected, context)

            //assert
            assert(stateLiveData.value is LoadingState)

        }

    @Test
    fun `invoke with bad response error state will be emitted`() =
        runBlockingTest {

            //arrange
            val cityName = "cairo"
            val isConnected = true
            val context = TestCoroutineDispatcher()
            useCase = GetWeatherDataUseCase(FakeRepositoryErrorResponse())

            //act
            useCase(cityName, stateLiveData, isConnected, context)

            //assert
            assert(stateLiveData.value is ErrorState)

        }

    @Test
    fun `invoke when exception thrown error state will be emitted`() =
        runBlockingTest {

            //arrange
            val cityName = "cairo"
            val isConnected = true
            val context = Dispatchers.Main

            //act
            useCase(cityName, stateLiveData, isConnected, context)

            //assert
            assert(stateLiveData.value is ErrorState)
        }
}