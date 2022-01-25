package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.Toxicity

/**
 * Simple implementation of [Toxicity].
 */
data class ToxicityImpl(
    override val id: Long,
    override val plantId: Long,
    override val animalId: AnimalType,
    override val toxic: ToxicityValue,
    override val source: String
) : Toxicity
