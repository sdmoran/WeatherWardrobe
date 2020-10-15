package com.cs4518.android.weatherwardrobe

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.cs4518.android.weatherwardrobe.weather.OpenWeatherFetchr
import com.cs4518.android.weatherwardrobe.weather.WeatherFragment
import com.cs4518.android.weatherwardrobe.weather.api.OpenWeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class SettingsFragmentHost : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.SummaryProvider<ListPreference>{

    private lateinit var navToWeather: Button
    private lateinit var navToCurrentGarb: Button
    private lateinit var navToWardrobe: Button
    private lateinit var navToSettings: Button
    var fusedLocationClient: FusedLocationProviderClient? = null
    private val wardrobeRepository = WardrobeRepository.get()
    private lateinit var geocoder: Geocoder
    private val states = hashMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkModeValues = resources.getStringArray(R.array.dark_mode_values)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.
        getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        updateStatesHash()

        if (checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )) {
            fusedLocationClient?.lastLocation?.
            addOnSuccessListener(
                this
            ) { location: Location? ->
                if (location == null) {
                    Log.d("LOCALITY", "No location")
                } else location.apply {
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    Log.d("LOCALITY", addresses[0].locality)


                    if (addresses.isNotEmpty()) {
                        wardrobeRepository.cityName = addresses[0].locality
                        wardrobeRepository.stateName = states[addresses[0].adminArea].toString()
                    }
                    Log.d("LOCALITY", "${location.latitude} ${location.longitude}")

                    wardrobeRepository.latitude = location.latitude
                    wardrobeRepository.longitude = location.longitude
                    Log.d("LOCALITY", "${wardrobeRepository.latitude} ${wardrobeRepository.longitude}")
                }
            }
            OpenWeatherFetchr().fetchWeatherData()
        }


        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = AppSettingsFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()

                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                PreferenceManager.getDefaultSharedPreferences(this)
                    .registerOnSharedPreferenceChangeListener(this)

        }

        navToWeather = findViewById(R.id.nav_button1)
        navToCurrentGarb = findViewById(R.id.nav_button2)
        navToWardrobe = findViewById(R.id.nav_button3)
        navToSettings = findViewById(R.id.nav_button4)

        navToWeather.setOnClickListener  {
            if (checkPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )) {
                fusedLocationClient?.lastLocation?.
                addOnSuccessListener(
                    this
                ) { location: Location? ->
                    if (location == null) {
                        Log.d("LOCALITY", "No location")
                    } else location.apply {
                        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        Log.d("LOCALITY", addresses[0].locality)


                        if (addresses.isNotEmpty()) {
                            wardrobeRepository.cityName = addresses[0].locality
                            wardrobeRepository.stateName = states[addresses[0].adminArea].toString()
                        }
                        Log.d("LOCALITY", "${location.latitude} ${location.longitude}")

                        wardrobeRepository.latitude = location.latitude
                        wardrobeRepository.longitude = location.longitude
                        Log.d("LOCALITY", "${wardrobeRepository.latitude} ${wardrobeRepository.longitude}")
                        val fragment = WeatherFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
            }
        }

        navToCurrentGarb.setOnClickListener  {
            val fragment = WeatherFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        navToWardrobe.setOnClickListener  {
            val fragment = WardrobeListFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        navToSettings.setOnClickListener  {
            val fragment = AppSettingsFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

    }
    private val permissionId = 42
    private fun checkPermission(vararg perm: String) : Boolean {
        val havePermissions = perm.toList().all {
            ContextCompat.checkSelfPermission(this, it) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (!havePermissions) {
            if(perm.toList().any {
                    ActivityCompat.
                    shouldShowRequestPermissionRationale(this, it)}
            ) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Permission")
                    .setMessage("Permission needed!")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this, perm, permissionId
                        )
                    }
                    .setNegativeButton("No") { _, _ -> }
                    .create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(this, perm, permissionId)
            }
            return false
        }
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val darkModeString = getString(R.string.dark_mode)
        key?.let {
            if (it == darkModeString) sharedPreferences?.let { pref ->
                val darkModeValues = resources.getStringArray(R.array.dark_mode_values)
                when (pref.getString(darkModeString, darkModeValues[0])) {
                    darkModeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    darkModeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkModeValues[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    darkModeValues[3] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
    }

    private fun updateStatesHash(){

        states["Alabama"] = "AL";
        states["Alaska"] = "AK";
        states["Alberta"] = "AB";
        states["American Samoa"] = "AS";
        states["Arizona"] = "AZ";
        states["Arkansas"] = "AR";
        states["Armed Forces (AE)"] = "AE";
        states["Armed Forces Americas"] = "AA";
        states["Armed Forces Pacific"] = "AP";
        states["British Columbia"] = "BC";
        states["California"] = "CA";
        states["Colorado"] = "CO";
        states["Connecticut"] = "CT";
        states["Delaware"] = "DE";
        states["District Of Columbia"] = "DC";
        states["Florida"] = "FL";
        states["Georgia"] = "GA";
        states["Guam"] = "GU";
        states["Hawaii"] = "HI";
        states["Idaho"] = "ID";
        states["Illinois"] = "IL";
        states["Indiana"] = "IN";
        states["Iowa"] = "IA";
        states["Kansas"] = "KS";
        states["Kentucky"] = "KY";
        states["Louisiana"] = "LA";
        states["Maine"] = "ME";
        states["Manitoba"] = "MB";
        states["Maryland"] = "MD";
        states["Massachusetts"] = "MA";
        states["Michigan"] = "MI";
        states["Minnesota"] = "MN";
        states["Mississippi"] = "MS";
        states["Missouri"] = "MO";
        states["Montana"] = "MT";
        states["Nebraska"] = "NE";
        states["Nevada"] = "NV";
        states["New Brunswick"] = "NB";
        states["New Hampshire"] = "NH";
        states["New Jersey"] = "NJ";
        states["New Mexico"] = "NM";
        states["New York"] = "NY";
        states["Newfoundland"] = "NF";
        states["North Carolina"] = "NC";
        states["North Dakota"] = "ND";
        states["Northwest Territories"] = "NT";
        states["Nova Scotia"] = "NS";
        states["Nunavut"] = "NU";
        states["Ohio"] = "OH";
        states["Oklahoma"] = "OK";
        states["Ontario"] = "ON";
        states["Oregon"] = "OR";
        states["Pennsylvania"] = "PA";
        states["Prince Edward Island"] = "PE";
        states["Puerto Rico"] = "PR";
        states["Quebec"] = "PQ";
        states["Rhode Island"] = "RI";
        states["Saskatchewan"] = "SK";
        states["South Carolina"] = "SC";
        states["South Dakota"] = "SD";
        states["Tennessee"] = "TN";
        states["Texas"] = "TX";
        states["Utah"] = "UT";
        states["Vermont"] = "VT";
        states["Virgin Islands"] = "VI";
        states["Virginia"] = "VA";
        states["Washington"] = "WA";
        states["West Virginia"] = "WV";
        states["Wisconsin"] = "WI";
        states["Wyoming"] = "WY";
        states["Yukon Territory"] = "YT";
    }

    class AppSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

        }
    }

    override fun provideSummary(preference: ListPreference?): CharSequence =
        if (preference?.key == getString(R.string.dark_mode)) preference.entry
        else "Unknown Preference"
}