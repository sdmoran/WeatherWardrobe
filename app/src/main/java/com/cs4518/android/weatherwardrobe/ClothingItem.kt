package com.cs4518.android.weatherwardrobe

import java.util.*

data class ClothingItem(val id: UUID = UUID.randomUUID(),
                        var clothingName: String = "",
                        var clothingType: String = "",
                        var clothingTempType: String = "",
                        var clothingAttribute: String = "")