package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.PlantFamily

data class PlantFamilyImpl(
    override val id: Long,
    override val family: String,
    override val plantId: Long,
    override val locale: String
    ) : PlantFamily