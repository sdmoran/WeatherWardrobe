package com.cs4518.android.weatherwardrobe

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.cs4518.android.weatherwardrobe.database.WardrobeDatabase
import com.cs4518.android.weatherwardrobe.weather.data.DayWeatherItem
import com.cs4518.android.weatherwardrobe.weather.formatPrecipitation
import com.cs4518.android.weatherwardrobe.weather.formatTemp
import com.cs4518.android.weatherwardrobe.weather.formatWind
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors
import kotlin.properties.Delegates

private const val DATABASE_NAME = "wardrobe-database"

class WardrobeRepository private constructor(context: Context) {

    private val database: WardrobeDatabase = Room.databaseBuilder(
        context.applicationContext,
        WardrobeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val wardrobeDao = database.wardrobeDao()

    private val executor = Executors.newSingleThreadExecutor()

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var cityName: String = ""
    var stateName: String = ""


    lateinit var dailyWeatherData: DayWeatherItem

    fun getTempMax(): String = formatTemp(dailyWeatherData.temp.max)
    fun getTempMin(): String = formatTemp(dailyWeatherData.temp.min)
    fun getTempDay(): String = formatTemp(dailyWeatherData.temp.day)
    fun getChanceOfRain(): String = formatPrecipitation(dailyWeatherData.pop)
    fun getWind(): String = formatWind(dailyWeatherData.wind_speed, dailyWeatherData.wind_deg)
    fun getUv(): String = dailyWeatherData.uvi.toInt().toString()

    fun getWardrobeItems(): LiveData<List<WardrobeItem>> = wardrobeDao.getWardrobeItems()

    fun getWardrobeItem(id: UUID): LiveData<WardrobeItem?> = wardrobeDao.getWardrobeItem(id)

    fun addWardrobeItem(wardrobeItem: WardrobeItem) {
        executor.execute {
            wardrobeDao.addWardrobeItem(wardrobeItem)
        }
    }

    fun addDummyData() {
        executor.execute {
            val items = listOf<WardrobeItem>(
                WardrobeItem(name = "Red Sweater", type = "Top", tags = "Warm,Fuzzy"),
                WardrobeItem(name = "Blue Jeans", type = "Bottom", tags = "Warm"),
                WardrobeItem(name = "Cargo Shorts", type = "Bottom", tags = "Cold"),
                WardrobeItem(name = "Straw Hat", type = "Hat", tags = "Accessory")
            )
            for(it in items) {
                wardrobeDao.addWardrobeItem(it)
            }
        }
    }

    fun clearDB() {
        executor.execute {
            wardrobeDao.clearDB()
        }
    }

    companion object {
        private var INSTANCE: WardrobeRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = WardrobeRepository(context)
            }
        }

        fun get(): WardrobeRepository {
            return INSTANCE ?:
                    throw IllegalStateException("WardrobeRepository must be initialized!")
        }
    }
}