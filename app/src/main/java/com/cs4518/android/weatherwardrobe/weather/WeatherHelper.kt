package com.cs4518.android.weatherwardrobe.weather

import com.cs4518.android.weatherwardrobe.R
import com.cs4518.android.weatherwardrobe.WardrobeRepository
import java.text.SimpleDateFormat
import java.util.*

private val wardrobeRepository = WardrobeRepository.get()


fun formatUVDesc(uv: Float): String{
    return when(uv.toInt()){
        in 0..2 -> "UV-Low"
        in 3..5 -> "UV-Moderate"
        in 6..7 -> "UV-High"
        else -> "UV-Very high"
    }

}
fun formatLocation(city: String, state: String): String{
    return "$city, $state"
}

fun formatDewPoint(dewPointTempKelvin: Float): String{
    return formatTemp(dewPointTempKelvin)
}

fun formatVisibility(visibility: Int): String{
    return when(wardrobeRepository.farenheit){
        true -> "${"%.2f".format(visibility / 1609.34)} Mi"
        else -> "${"%.2f".format((visibility / 1000.00))} Km"
    }
}

fun formatPressure(pressure: Int): String{
    return "${"%.2f".format((pressure + 101325) / 3386.38)}\""
}

fun formatPrecipitation(probabilityPrecip: Float): String{
    return "${(probabilityPrecip*100).toInt()}%"
}

fun formatMinMax(minTempKelvin: Float, maxTempKelvin: Float): String{
    return "${formatTemp(maxTempKelvin)}/${formatTemp(minTempKelvin)}"
}

fun formatTemp(tempKelvin: Float) : String {
    return when(wardrobeRepository.farenheit){
        true -> "${((tempKelvin - 273.15) * 9.0/5.0 + 32).toInt()}°"
        else -> "${(tempKelvin - 273.15).toInt()}°"
    }
}

fun formatFeelsLike(feelsLike: Float): String{
    return "Feelslike ${formatTemp(feelsLike)}"
}

fun formatHumidity(humidity: Int): String{
    return "$humidity%"
}
fun formatWind(windSpeed: Float, windDeg: Int): String{
    val index = ((windDeg/22.5)+.5).toInt()
    val arr = arrayOf(
        "N",
        "NNE",
        "NE",
        "ENE",
        "E",
        "ESE",
        "SE",
        "SSE",
        "S",
        "SSW",
        "SW",
        "WSW",
        "W",
        "WNW",
        "NW",
        "NNW"
    )
    val modWindSpeed = (windSpeed*2.23694).toInt()
    return when(wardrobeRepository.farenheit){
        true -> "${arr[(index % 16)]} $modWindSpeed MPH"
        else -> "${arr[(index % 16)]} ${(modWindSpeed*1.609).toInt()} KPH"
    }
}

fun weatherIcon(weatherItemIcon: String): Int{
    return when(weatherItemIcon){
        "01d" -> R.drawable.a01d
        "01n" -> R.drawable.a01n
        "02d" -> R.drawable.a02d
        "02n" -> R.drawable.a02n
        "03d" -> R.drawable.a03d
        "03n" -> R.drawable.a03n
        "04d" -> R.drawable.a04d
        "04n" -> R.drawable.a04n
        "09d" -> R.drawable.a09d
        "09n" -> R.drawable.a09n
        "10d" -> R.drawable.a10d
        "10n" -> R.drawable.a10n
        "11d" -> R.drawable.a11d
        "11n" -> R.drawable.a11n
        "13d" -> R.drawable.a13d
        "13n" -> R.drawable.a13npng
        "50d" -> R.drawable.a50d
        "50n" -> R.drawable.a50n
        else -> R.drawable.a01d
    }
}
fun formatDate(dt: Long, format: String): String{
    val sdf = SimpleDateFormat(format)
    val netDate = Date(dt * 1000L)
    val date = sdf.format(netDate)

    if(date.startsWith('0')) return date.substring(1)
    return date
}