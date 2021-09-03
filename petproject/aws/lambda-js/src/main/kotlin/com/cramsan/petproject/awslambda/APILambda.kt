package com.cramsan.petproject.awslambda

import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily

class APILambda {

    fun plants() = modelStorage.getPlants()

    fun mainNames() = modelStorage.getPlantsMainName()

    fun commonNames(plantId: Long): List<PlantCommonName> {
        val list = modelStorage.getPlantsCommonNames()
        return list.filter { it.plantId == plantId }
    }

    fun family(plantId: Long): PlantFamily {
        val list = modelStorage.getPlantsFamily()
        return list.first { it.plantId == plantId }
    }

    fun description(plantId: Long, animalType: Int): Description {
        val list = modelStorage.getDescription()
        return list.first { it.plantId == plantId && it.animalId.ordinal == animalType }
    }

    fun toxicities() = modelStorage.getToxicity()

    companion object {
        private val dependenciesConfig by lazy { DependenciesConfig() }
        val modelStorage by lazy { dependenciesConfig.modelStorage }
    }
}
