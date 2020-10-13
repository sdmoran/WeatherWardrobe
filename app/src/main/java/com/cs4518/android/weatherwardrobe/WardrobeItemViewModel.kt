package com.cs4518.android.weatherwardrobe

import androidx.lifecycle.ViewModel

class WardrobeItemViewModel : ViewModel() {

    var item: WardrobeItem? = null
        set(item) {
            field = item
        }

    val name: String?
        get() = item?.name

    val type: String?
        get() = item?.type

    val tags: String?
        get() = item?.tags
}