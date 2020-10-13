package com.cs4518.android.weatherwardrobe

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.databinding.ActivityWeatherWardrobeBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding

private const val TAG = "WeatherWardrobeActivity"

class WeatherWardrobeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_wardrobe)

        val isFragmentContainerEmpty = savedInstanceState == null

        if(isFragmentContainerEmpty) {
            Log.d(TAG, "Fragment container empty! Adding!")
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, WardrobeListFragment.newInstance())
                .commit()
        }
    }

}