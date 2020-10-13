package com.cs4518.android.weatherwardrobe

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class WardrobeItem(@PrimaryKey val id: UUID = UUID.randomUUID(),
                                    var name: String = "",
                                    var type: String = "",
                                    var tags: String = "" //MutableList<String> = mutableListOf()
                        ) {
    val fileName
        get() = "IMG_${id}.jpg"
}

