package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.PlantMainName

data class PlantMainNameImpl(
    override val id: Long,
    override val mainName: String,
    override val plantId: Long,
    override val locale: String
) : PlantMainName
