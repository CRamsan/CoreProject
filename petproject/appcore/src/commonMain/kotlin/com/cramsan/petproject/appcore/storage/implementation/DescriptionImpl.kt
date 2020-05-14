package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.Description

data class DescriptionImpl(
    override val id: Long,
    override val plantId: Long,
    override val animalId: AnimalType,
    override val locale: String,
    override val description: String
) : Description
