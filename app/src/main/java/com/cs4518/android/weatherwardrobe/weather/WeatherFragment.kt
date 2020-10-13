package com.cs4518.android.weatherwardrobe.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.R
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse
import com.cs4518.android.weatherwardrobe.weather.data.HourlyWeatherItem
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

private const val TAG = "WeatherFragment"

class WeatherFragment : Fragment() {

    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModel =
            ViewModelProviders.of(this).get(WeatherViewModel::class.java)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.weatherLiveData.observe(
            viewLifecycleOwner,
            Observer { weatherResponse ->
                weatherRecyclerView.adapter = WeatherHourlyAdapter(weatherResponse)
            })
    }

    private class WeatherHourlyHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        private lateinit var hourlyData: HourlyWeatherItem
        val hourlyHourTextView: TextView = itemView.findViewById(R.id.hourly_hour)
        val hourlyTempTextView: TextView = itemView.findViewById(R.id.hourly_temp)
        val hourlyDateTextView: TextView = itemView.findViewById(R.id.hourly_date)
        val hourlyFeelsLikeTextView: TextView = itemView.findViewById(R.id.hourly_feels_like)
        val hourlyHumidityTextView: TextView = itemView.findViewById(R.id.hourly_humidity)
        val hourlyWindTextView: TextView = itemView.findViewById(R.id.hourly_wind)
    }

    private inner class WeatherHourlyAdapter(private val weatherResponse: OpenWeatherResponse)
        : RecyclerView.Adapter<WeatherHourlyHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): WeatherHourlyHolder {
            val view = layoutInflater.inflate(R.layout.list_item_hourly, parent, false)
            return WeatherHourlyHolder(view)
        }

        override fun onBindViewHolder(holder: WeatherHourlyHolder, position: Int) {
            val hourlyData = weatherResponse.hourly[position]
            val hour: String = formatDate(hourlyData.dt, "hh a")
            val date: String = formatDate(hourlyData.dt, "MMM dd")
            val temp: String = formatTemp(hourlyData.temp)
            val feelsLike: String = formatFeelsLike(hourlyData.feels_like)
            val humidity: String = formatHumidity(hourlyData.humidity)
            val wind: String = formatWind(hourlyData.wind_speed, hourlyData.wind_deg)

            holder.hourlyHourTextView.text = hour
            holder.hourlyTempTextView.text = temp
            holder.hourlyDateTextView.text = date
            holder.hourlyFeelsLikeTextView.text = feelsLike
            holder.hourlyHumidityTextView.text = humidity
            holder.hourlyWindTextView.text = wind
        }

        private fun formatTemp(tempKelvin: Float) : String {
            return "${((tempKelvin - 273.15) * 9.0/5.0 + 32).toInt()}°"
        }

        private fun formatDate(dt: Long, format: String): String{
            val sdf = SimpleDateFormat(format)
            val netDate = Date(dt*1000L)
            val date = sdf.format(netDate)

            if(date.startsWith('0')) return date.substring(1)
            return date
        }

        private fun formatFeelsLike(feelsLike: Float): String{
            return "Feelslike ${formatTemp(feelsLike)}"
        }

        private fun formatHumidity(humidity: Int): String{
            return "Humidity $humidity%"
        }
        private fun formatWind(windSpeed: Float, windDeg: Int): String{
            val index = ((windDeg/22.5)+.5).toInt()
            val arr = arrayOf("N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW")
            val modWindSpeed = (windSpeed*2.23694).toInt()
            return "${arr[(index % 16)]} $modWindSpeed MPH"
        }

        override fun getItemCount(): Int = weatherResponse.hourly.size
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        weatherRecyclerView = view.findViewById(R.id.weather_recycler_view)
        weatherRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }



    companion object {
        fun newInstance() = WeatherFragment()
    }
}