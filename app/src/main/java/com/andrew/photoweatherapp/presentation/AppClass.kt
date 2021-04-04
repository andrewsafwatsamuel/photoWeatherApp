package com.andrew.photoweatherapp.presentation

import android.app.Application
import android.content.Context

lateinit var appContext:Context

class AppClass:Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}