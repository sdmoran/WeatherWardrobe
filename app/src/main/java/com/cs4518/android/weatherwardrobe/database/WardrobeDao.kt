package com.cs4518.android.weatherwardrobe.database

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("SELECT EXISTS(SELECT * FROM wardrobeitem where id =(:id))")
    fun itemExists(id: UUID) : Boolean

    @Update
    fun updateWardrobeItem(wardrobeItem: WardrobeItem)

    @Insert
    fun addWardrobeItem(wardrobeItem: WardrobeItem)

    @Query("DELETE FROM wardrobeitem")
    fun clearDB()

}