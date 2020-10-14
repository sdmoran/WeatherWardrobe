package com.cs4518.android.weatherwardrobe

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import retrofit2.http.GET

class WardrobeItemViewModel : BaseObservable() {

    var item: WardrobeItem? = null
        set(item) {
            field = item
            notifyChange()
        }

    @get:Bindable
    val name: String?
        get() = item?.name

    @get:Bindable
    val type: String?
        get() = item?.type

    @get:Bindable
    val tags: String?
        get() = item?.tags
}