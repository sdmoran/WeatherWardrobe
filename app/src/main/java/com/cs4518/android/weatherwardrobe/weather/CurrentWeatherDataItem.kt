package com.cs4518.android.weatherwardrobe.weather

data class CurrentWeatherDataItem(
    var dt: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: Float,
    var feels_like: Float,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Float,
    var uvi: Float,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Float,
    var wind_deg: Int,
    var wind_gust: Float,
    var weather: List<WeatherItem>)