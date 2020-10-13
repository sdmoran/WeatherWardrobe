package com.cs4518.android.weatherwardrobe.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherApi
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "OpenWeatherFetchr"

class OpenWeatherFetchr {

    private val openWeatherApi: OpenWeatherApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openWeatherApi = retrofit.create(OpenWeatherApi::class.java)
    }

    fun fetchWeatherData(): LiveData<OpenWeatherResponse> {
        val responseLiveData: MutableLiveData<OpenWeatherResponse> = MutableLiveData()
        val openWeatherRequest: Call<OpenWeatherResponse> = openWeatherApi.fetchWeatherData()

        openWeatherRequest.enqueue(object : Callback<OpenWeatherResponse> {

            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch weather data", t)
            }

            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                Log.d(TAG, "Response received")
                val openWeatherResponse: OpenWeatherResponse? = response.body()
                val weatherResponse: OpenWeatherResponse? = openWeatherResponse
                responseLiveData.value = weatherResponse
            }
        })

        return responseLiveData
    }
}