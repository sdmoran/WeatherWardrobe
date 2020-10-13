package com.cs4518.android.weatherwardrobe.weather

data class WeatherDataItem(
    var lat: Float,
    var long: Float,
    var timezone: String,
    var timezone_offset: Int,
    var current: CurrentWeatherDataItem,
    var hourly: List<HourlyWeatherItem>,
    var daily: List<DayWeatherItem>
)