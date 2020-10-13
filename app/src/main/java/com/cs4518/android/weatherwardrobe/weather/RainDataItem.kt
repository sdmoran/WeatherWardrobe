package com.cs4518.android.weatherwardrobe.weather

import com.google.gson.annotations.SerializedName

data class RainDataItem(
    @SerializedName("1h") var hr1 : Float
)