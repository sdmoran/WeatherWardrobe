package com.cs4518.android.weatherwardrobe.weather.data

import com.cs4518.android.weatherwardrobe.weather.data.CurrentWeatherDataItem
import com.cs4518.android.weatherwardrobe.weather.data.DayWeatherItem
import com.cs4518.android.weatherwardrobe.weather.data.HourlyWeatherItem

data class WeatherDataItem(
    var lat: Float,
    var long: Float,
    var timezone: String,
    var timezone_offset: Int,
    var current: CurrentWeatherDataItem,
    var hourly: List<HourlyWeatherItem>,
    var daily: List<DayWeatherItem>
)