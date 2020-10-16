package com.cs4518.android.weatherwardrobe

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.cs4518.android.weatherwardrobe.database.WardrobeDatabase
import com.cs4518.android.weatherwardrobe.weather.data.DayWeatherItem
import com.cs4518.android.weatherwardrobe.weather.formatPrecipitation
import com.cs4518.android.weatherwardrobe.weather.formatTemp
import com.cs4518.android.weatherwardrobe.weather.formatWind
import java.io.File
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
    private val filesDir = context.applicationContext.filesDir

    var latitude: Double = 42.262592
    var longitude: Double = -71.802292
    var cityName: String = "Worcester"
    var stateName: String = "MA"
    var doneExecuting: Boolean = false
    var executed: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var pressedGarb: Boolean = false
    var farenheit: Boolean = true
    var state: String = "Settings"

    lateinit var dailyWeatherData: DayWeatherItem

    fun getTempMax(): String = formatTemp(dailyWeatherData.temp.max)
    fun getTempMin(): String = formatTemp(dailyWeatherData.temp.min)
    fun getTempDay(): String = formatTemp(dailyWeatherData.temp.day)
    fun getChanceOfRain(): String = formatPrecipitation(dailyWeatherData.pop)
    fun getWind(): String = formatWind(dailyWeatherData.wind_speed, dailyWeatherData.wind_deg)
    fun getUv(): String = dailyWeatherData.uvi.toInt().toString()

    fun getNumItems(): Int = wardrobeDao.getNumItems()

    fun getWardrobeItems(): LiveData<List<WardrobeItem>> = wardrobeDao.getWardrobeItems()

    fun getWardrobeItem(id: UUID): LiveData<WardrobeItem?> = wardrobeDao.getWardrobeItem(id)

    // Guaranteed to return a WardrobeItem. If it doesn't yet exist, makes it, then returns.
    // Necessary for photoURI to work as intended when adding a brand new ClothingItem to
    // wardrobe.
    fun guaranteeGetWardrobeItem(id: UUID): LiveData<WardrobeItem?> {
        executor.execute {
            val exists = wardrobeDao.itemExists(id)
            if(!exists) {
                addWardrobeItem(WardrobeItem(id))
            }
        }
        return getWardrobeItem(id)
    }

    fun updateWarDrobeItem(wardrobeItem: WardrobeItem) {
        executor.execute {
            wardrobeDao.updateWardrobeItem(wardrobeItem)
        }
    }

    fun addWardrobeItem(wardrobeItem: WardrobeItem) {
        executor.execute {
            wardrobeDao.addWardrobeItem(wardrobeItem)
        }
    }

    fun addOrUpdateItem(wardrobeItem: WardrobeItem) {
        executor.execute {
            val exists = wardrobeDao.itemExists(wardrobeItem.id)
            if(!exists) {
                addWardrobeItem(wardrobeItem)
            }
            else {
                updateWarDrobeItem(wardrobeItem)
            }
        }
    }

    fun getPhotoFile(wardrobeItem: WardrobeItem): File = File(filesDir, wardrobeItem.photoFileName)

    fun addDummyData() {
        executor.execute {
            val items = listOf<WardrobeItem>(
                WardrobeItem(name = "Red Sweater", type = "Top", tags = "Warm,Fuzzy"),
                WardrobeItem(name = "Blue Jeans", type = "Bottom", tags = "Warm"),
                WardrobeItem(name = "Cargo Shorts", type = "Bottom", tags = "Cool"),
                WardrobeItem(name = "Straw Hat", type = "Accessory", tags = "Cool")
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
                Executors.newSingleThreadExecutor().execute {
                    val r = get()
                    if(r.getNumItems() == 0) {
                        r.addDummyData()
                    }
                }
            }
        }

        fun get(): WardrobeRepository {
            return INSTANCE ?:
                    throw IllegalStateException("WardrobeRepository must be initialized!")
        }
    }
}