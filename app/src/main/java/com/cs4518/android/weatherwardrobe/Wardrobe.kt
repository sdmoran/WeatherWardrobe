package com.cs4518.android.weatherwardrobe

import androidx.lifecycle.LiveData

class Wardrobe() {

    val items: LiveData<List<WardrobeItem>>

    init {
        items = loadItems()
    }

    private fun loadItems(): LiveData<List<WardrobeItem>> {

        val liveDataItems = WardrobeRepository.get().getWardrobeItems()

        return liveDataItems
    }
}