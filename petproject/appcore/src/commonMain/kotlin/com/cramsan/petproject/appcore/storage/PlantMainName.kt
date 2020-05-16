package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

interface PlantMainName {
    val id: Long

    val mainName: String

    val plantId: Long

    val locale: String
}
