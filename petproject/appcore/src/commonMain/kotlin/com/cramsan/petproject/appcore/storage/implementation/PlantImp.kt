package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.Plant

data class PlantImp(
    override val id: Long,
    override val scientificName: String,
    override val imageUrl: String
) : Plant
