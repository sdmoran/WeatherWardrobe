package com.cs4518.android.weatherwardrobe.weather.data

data class DayWeatherItem(
    var dt: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: TempDataItem,
    var feels_like: FeelsLikeItem,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Float,
    var wind_speed: Float,
    var wind_deg: Int,
    var weather: List<WeatherItem>,
    var clouds: Int,
    var pop: Float,
    var rain: Float,
    var uvi: Float
)