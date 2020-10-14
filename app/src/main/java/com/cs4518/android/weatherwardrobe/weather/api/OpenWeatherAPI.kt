package com.cs4518.android.weatherwardrobe.weather.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface OpenWeatherApi {

    @GET
    fun fetchWeatherData(@Url url: String): Call<OpenWeatherResponse>
}