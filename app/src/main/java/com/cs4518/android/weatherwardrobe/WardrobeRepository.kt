package com.cs4518.android.weatherwardrobe

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.cs4518.android.weatherwardrobe.database.WardrobeDatabase
import com.cs4518.android.weatherwardrobe.weather.data.DayWeatherItem
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "wardrobe-database"

class WardrobeRepository private constructor(context: Context) {

    private val database: WardrobeDatabase = Room.databaseBuilder(
        context.applicationContext,
        WardrobeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val wardrobeDao = database.wardrobeDao()

    private val executor = Executors.newSingleThreadExecutor()

    lateinit var dailyWeatherData: DayWeatherItem
    fun getWardrobeItems(): LiveData<List<WardrobeItem>> = wardrobeDao.getWardrobeItems()

    fun getWardrobeItem(id: UUID): LiveData<WardrobeItem?> = wardrobeDao.getWardrobeItem(id)

    fun addWardrobeItem(wardrobeItem: WardrobeItem) {
        executor.execute {
            wardrobeDao.addWardrobeItem(wardrobeItem)
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