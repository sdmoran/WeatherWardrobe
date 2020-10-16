package com.cs4518.android.weatherwardrobe.weather.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cs4518.android.weatherwardrobe.WardrobeItem
import com.cs4518.android.weatherwardrobe.WardrobeRepository
import java.io.File
import java.util.*

class WardrobeItemDetailViewModel() : ViewModel() {

    private val warDrobeRepository = WardrobeRepository.get()
    private val warDrobeIDLiveData = MutableLiveData<UUID>()

    var wardrobeLiveData: LiveData<WardrobeItem?> =
        Transformations.switchMap(warDrobeIDLiveData) { id ->
            warDrobeRepository.getWardrobeItem(id)
        }

    fun loadWardrobeItem(id: UUID) {
        warDrobeIDLiveData.value = id
    }

    fun saveWarDrobeItem(wardrobeItem: WardrobeItem) {
        warDrobeRepository.updateWarDrobeItem(wardrobeItem)
    }

    fun getPhotoFile(wardrobeItem: WardrobeItem): File {
        return warDrobeRepository.getPhotoFile(wardrobeItem)
    }
}