package com.cs4518.android.weatherwardrobe

import android.app.Application

class WeatherWardrobeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WardrobeRepository.initialize(this)
    }
}