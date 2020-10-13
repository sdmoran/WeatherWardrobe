package com.cs4518.android.weatherwardrobe.weather.api

import com.cs4518.android.weatherwardrobe.weather.data.CurrentWeatherDataItem
import com.cs4518.android.weatherwardrobe.weather.data.DayWeatherItem
import com.cs4518.android.weatherwardrobe.weather.data.HourlyWeatherItem

class OpenWeatherResponse {
    lateinit var current: CurrentWeatherDataItem
    lateinit var hourly: List<HourlyWeatherItem>
    lateinit var daily: List<DayWeatherItem>
}