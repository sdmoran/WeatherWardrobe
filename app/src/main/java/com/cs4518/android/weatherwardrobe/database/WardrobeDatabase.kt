package com.cs4518.android.weatherwardrobe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cs4518.android.weatherwardrobe.WardrobeItem

@Database(entities = [ WardrobeItem:: class], version=1)
@TypeConverters(WardrobeItemTypeConverters::class)
abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun wardrobeDao(): WardrobeDao

}