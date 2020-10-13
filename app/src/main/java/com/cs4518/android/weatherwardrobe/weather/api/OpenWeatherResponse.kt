package com.cs4518.android.weatherwardrobe.weather.api

import com.cs4518.android.weatherwardrobe.weather.CurrentWeatherDataItem
import com.cs4518.android.weatherwardrobe.weather.DayWeatherItem
import com.cs4518.android.weatherwardrobe.weather.HourlyWeatherItem

class OpenWeatherResponse {
    lateinit var current: CurrentWeatherDataItem
    lateinit var hourly: List<HourlyWeatherItem>
    lateinit var daily: List<DayWeatherItem>
}