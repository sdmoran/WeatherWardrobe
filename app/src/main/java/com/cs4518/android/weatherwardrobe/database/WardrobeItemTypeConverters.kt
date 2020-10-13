package com.cs4518.android.weatherwardrobe.database

import androidx.room.TypeConverter
import java.util.*

class WardrobeItemTypeConverters {

//    @TypeConverter
//    fun fromList(ls: MutableList<String>): String {
//
//    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}