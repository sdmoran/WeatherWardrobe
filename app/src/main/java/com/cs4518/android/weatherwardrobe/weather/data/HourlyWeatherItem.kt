package com.cs4518.android.weatherwardrobe.weather.data

data class HourlyWeatherItem(
    var dt: Long,
    var temp: Float,
    var feels_like: Float,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Float,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Float,
    var wind_deg: Int,
    var weather: List<WeatherItem>,
    var pop: Float,
    var rain: RainDataItem
)