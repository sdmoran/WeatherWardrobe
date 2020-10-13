package com.cs4518.android.weatherwardrobe.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cs4518.android.weatherwardrobe.R

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, WeatherFragment.newInstance())
                .commit()
        }
    }
}