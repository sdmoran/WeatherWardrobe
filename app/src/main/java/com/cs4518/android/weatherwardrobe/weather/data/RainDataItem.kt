package com.cs4518.android.weatherwardrobe.weather.data

import com.google.gson.annotations.SerializedName

data class RainDataItem(
    @SerializedName("1h") var hr1 : Float
)