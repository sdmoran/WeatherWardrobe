package com.cs4518.android.weatherwardrobe.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.R
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse

private const val TAG = "WeatherFragment"

class WeatherFragment : Fragment() {

    private lateinit var weatherRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val openWeatherLiveData: LiveData<OpenWeatherResponse> = OpenWeatherFetchr().fetchWeatherData()
        openWeatherLiveData.observe(
            this,
            Observer { weatherResponse ->
                Log.d(TAG, "Response received: ${weatherResponse.current.sunrise}")
            })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        weatherRecyclerView = view.findViewById(R.id.weather_recycler_view)
        weatherRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}