package com.cramsan.petproject.appcore.storage

import kotlin.Long
import kotlin.String

interface Plant {
    val id: Long

    val scientificNames: String

    val imageUrl: String
}
