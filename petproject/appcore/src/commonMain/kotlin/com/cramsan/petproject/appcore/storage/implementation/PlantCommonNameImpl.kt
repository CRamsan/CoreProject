package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.PlantCommonName

data class PlantCommonNameImpl(
    override val id: Long,
    override val commonName: String,
    override val plantId: Long,
    override val locale: String
) : PlantCommonName
