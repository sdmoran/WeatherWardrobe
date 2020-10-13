package com.cs4518.android.weatherwardrobe.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse

class WeatherViewModel : ViewModel() {

    val weatherLiveData: LiveData<OpenWeatherResponse> = OpenWeatherFetchr().fetchWeatherData()

}