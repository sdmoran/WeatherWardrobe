package com.cs4518.android.weatherwardrobe.weather.api

import retrofit2.Call
import retrofit2.http.GET

interface OpenWeatherApi {

    @GET(
        "data/2.5/onecall?lat=42.262592" +
                "&lon=-71.802292" +
                "&exclude=minutely" +
                "&appid=f18eebbbbccb30c0097b6bc2f920188d"
    )
    fun fetchWeatherData(): Call<OpenWeatherResponse>
}