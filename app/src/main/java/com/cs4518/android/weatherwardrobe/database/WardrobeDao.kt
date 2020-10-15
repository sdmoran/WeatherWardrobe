package com.cs4518.android.weatherwardrobe.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cs4518.android.weatherwardrobe.WardrobeItem
import java.util.*

@Dao
interface WardrobeDao {
    @Query("SELECT * FROM wardrobeitem")
    fun getWardrobeItems(): LiveData<List<WardrobeItem>>

    @Query("SELECT count(*) FROM wardrobeitem")
    fun getNumItems(): Int

    @Query("SELECT * FROM wardrobeitem WHERE id=(:id)")
    fun getWardrobeItem(id: UUID): LiveData<WardrobeItem?>

    @Insert
    fun addWardrobeItem(wardrobeItem: WardrobeItem)

    @Query("DELETE FROM wardrobeitem")
    fun clearDB()

}