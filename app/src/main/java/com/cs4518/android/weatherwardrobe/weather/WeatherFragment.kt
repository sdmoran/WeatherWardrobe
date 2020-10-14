package com.cs4518.android.weatherwardrobe.weather

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cs4518.android.weatherwardrobe.R
import com.cs4518.android.weatherwardrobe.SettingsFragmentHost
import com.cs4518.android.weatherwardrobe.WardrobeRepository
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "WeatherFragment"

class WeatherFragment : Fragment() {

    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var weatherViewModel: WeatherViewModel
    private val wardrobeRepository = WardrobeRepository.get()

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
                weatherRecyclerView.adapter = ComplexRecyclerViewAdapter(weatherResponse)
                wardrobeRepository.dailyWeatherData = weatherResponse.daily.first()
            })
    }

    private class ComplexRecyclerViewAdapter(private val weatherResponse: OpenWeatherResponse) :
        RecyclerView.Adapter<ViewHolder>() {
        private val wardrobeRepository = WardrobeRepository.get()

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount(): Int {
            return 48
        }

        override fun getItemViewType(position: Int): Int {
            return when(position) {
                0 -> 0
                else -> 1
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val viewHolder: ViewHolder
            val inflater = LayoutInflater.from(viewGroup.context)

            viewHolder = when (viewType) {
                0 -> {
                    val v1: View = inflater.inflate(R.layout.list_item_current, viewGroup, false)
                    WeatherCurrentHolder(v1)
                }
                else -> {
                    val v2: View = inflater.inflate(R.layout.list_item_hourly, viewGroup, false)
                    WeatherHourlyHolder(v2)
                }
            }
            return viewHolder
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            when (viewHolder.itemViewType) {
                0 -> {
                    val holder: WeatherCurrentHolder = viewHolder as WeatherCurrentHolder
                    val currentData = weatherResponse.current
                    val location = formatLocation(wardrobeRepository.cityName, wardrobeRepository.stateName)
                    val temp: String = formatTemp(currentData.temp)
                    val feelsLike: String = formatFeelsLike(currentData.feels_like)
                    val humidity: String = formatHumidity(currentData.humidity)
                    val wind: String = formatWind(currentData.wind_speed, currentData.wind_deg)
                    val weatherResourceIcon: Int = weatherIcon(currentData.weather.first().icon)
                    val description: String = currentData.weather.first().description.capitalize()
                    val minMax: String = formatMinMax(
                        weatherResponse.daily.first().temp.min,
                        weatherResponse.daily.first().temp.max
                    )
                    val precipitation: String = formatPrecipitation(weatherResponse.hourly.first().pop)
                    val uv: String = currentData.uvi.toInt().toString()
                    val pressure: String = formatPressure(currentData.pressure)
                    val visibility: String = formatVisibility(currentData.visibility)
                    val dewPoint: String = formatDewPoint(currentData.dew_point)


                    holder.currentLocation.text = location
                    holder.currentTempTextView.text = temp
                    holder.currentFeelsLikeTextView.text = feelsLike
                    holder.currentHumidityTextView.text = humidity
                    holder.currentWindTextView.text = wind
                    holder.currentIconImageView.setImageResource(weatherResourceIcon)
                    holder.currentDescription.text = description
                    holder.currentMinMaxTextView.text = minMax
                    holder.currentPrecipitationTextView.text = precipitation
                    holder.currentUvTextView.text = uv
                    holder.currentPressureTextView.text = pressure
                    holder.currentVisibilityTextView.text = visibility
                    holder.currentDewPointTextView.text = dewPoint
                }
                else -> {
                    val holder: WeatherHourlyHolder = viewHolder as WeatherHourlyHolder
                    val hourlyData = weatherResponse.hourly[position]
                    val hour: String = formatDate(hourlyData.dt, "hh a")
                    val date: String = formatDate(hourlyData.dt, "MMM dd")
                    val temp: String = formatTemp(hourlyData.temp)
                    val feelsLike: String = formatFeelsLike(hourlyData.feels_like)
                    val precipitation: String = formatPrecipitation(hourlyData.pop)
                    val wind: String = formatWind(hourlyData.wind_speed, hourlyData.wind_deg)
                    val weatherResourceIcon: Int = weatherIcon(hourlyData.weather.first().icon)
                    val description: String = hourlyData.weather.first().description.capitalize()


                    holder.hourlyHourTextView.text = hour
                    holder.hourlyTempTextView.text = temp
                    holder.hourlyDateTextView.text = date
                    holder.hourlyFeelsLikeTextView.text = feelsLike
                    holder.hourlyPrecipitationTextView.text = precipitation
                    holder.hourlyWindTextView.text = wind
                    holder.hourlyIconImageView.setImageResource(weatherResourceIcon)
                    holder.hourlyDescription.text = description
                }
            }
        }
        private fun formatLocation(city: String, state: String): String{
            return "$city, $state"
        }

        private fun formatDewPoint(dewPointTempKelvin: Float): String{
            return formatTemp(dewPointTempKelvin)
        }

        private fun formatVisibility(visibility: Int): String{
            return "${"%.2f".format(visibility / 1609.34)} Mi"
        }

        private fun formatPressure(pressure: Int): String{
            return "${"%.2f".format((pressure + 101325) / 3386.38)}\""
        }

        private fun formatPrecipitation(probabilityPrecip: Float): String{
            return "${(probabilityPrecip*100).toInt()}%"
        }

        private fun formatMinMax(minTempKelvin: Float, maxTempKelvin: Float): String{
            return "${formatTemp(maxTempKelvin)}/${formatTemp(minTempKelvin)}"
        }

        private fun formatTemp(tempKelvin: Float) : String {
            return "${((tempKelvin - 273.15) * 9.0/5.0 + 32).toInt()}°"
        }

        private fun formatFeelsLike(feelsLike: Float): String{
            return "Feelslike ${formatTemp(feelsLike)}"
        }

        private fun formatHumidity(humidity: Int): String{
            return "$humidity%"
        }
        private fun formatWind(windSpeed: Float, windDeg: Int): String{
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
            return "${arr[(index % 16)]} $modWindSpeed MPH"
        }

        private fun weatherIcon(weatherItemIcon: String): Int{
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
        private fun formatDate(dt: Long, format: String): String{
            val sdf = SimpleDateFormat(format)
            val netDate = Date(dt * 1000L)
            val date = sdf.format(netDate)

            if(date.startsWith('0')) return date.substring(1)
            return date
        }
    }

    private class WeatherCurrentHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        val currentTempTextView: TextView = itemView.findViewById(R.id.current_temp)
        val currentFeelsLikeTextView: TextView = itemView.findViewById(R.id.current_feels_like)
        val currentHumidityTextView: TextView = itemView.findViewById(R.id.current_humidity)
        val currentWindTextView: TextView = itemView.findViewById(R.id.current_wind)
        val currentIconImageView: ImageView = itemView.findViewById(R.id.current_icon)
        val currentDescription: TextView = itemView.findViewById(R.id.current_description)
        val currentMinMaxTextView: TextView = itemView.findViewById(R.id.current_minmax)
        val currentUvTextView: TextView = itemView.findViewById(R.id.current_uv)
        val currentPrecipitationTextView:TextView = itemView.findViewById(R.id.current_precipitation)
        val currentPressureTextView:TextView = itemView.findViewById(R.id.current_pressure)
        val currentVisibilityTextView:TextView = itemView.findViewById(R.id.current_visibility)
        val currentDewPointTextView:TextView = itemView.findViewById(R.id.current_dew_point)
        val currentLocation:TextView = itemView.findViewById(R.id.current_location)
    }


    private class WeatherHourlyHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        val hourlyHourTextView: TextView = itemView.findViewById(R.id.hourly_hour)
        val hourlyTempTextView: TextView = itemView.findViewById(R.id.hourly_temp)
        val hourlyDateTextView: TextView = itemView.findViewById(R.id.hourly_date)
        val hourlyFeelsLikeTextView: TextView = itemView.findViewById(R.id.hourly_feels_like)
        val hourlyPrecipitationTextView: TextView = itemView.findViewById(R.id.hourly_precipitation)
        val hourlyWindTextView: TextView = itemView.findViewById(R.id.hourly_wind)
        val hourlyIconImageView: ImageView = itemView.findViewById(R.id.hourly_icon)
        val hourlyDescription: TextView = itemView.findViewById(R.id.hourly_description)
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