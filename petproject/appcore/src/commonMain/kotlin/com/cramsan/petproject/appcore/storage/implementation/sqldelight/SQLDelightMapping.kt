package com.cramsan.petproject.appcore.storage.implementation.sqldelight

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.db.Description
import com.cramsan.petproject.db.GetAllPlantsWithAnimalId
import com.cramsan.petproject.db.GetAllPlantsWithAnimalIdAll
import com.cramsan.petproject.db.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.db.Plant
import com.cramsan.petproject.db.PlantCommonName
import com.cramsan.petproject.db.PlantFamily
import com.cramsan.petproject.db.PlantMainName
import com.cramsan.petproject.db.Toxicity

class Description(private val plantDescription: Description) : com.cramsan.petproject.appcore.storage.Description {
    override val id: Long
        get() = plantDescription.id
    override val description: String
        get() = plantDescription.description
    override val plantId: Long
        get() = plantDescription.plant_id
    override val animalId: AnimalType
        get() = plantDescription.animal_id
    override val locale: String
        get() = plantDescription.locale
}

class Plant(private val plant: Plant) : com.cramsan.petproject.appcore.storage.Plant {
    override val id: Long
        get() = plant.id
    override val scientificName: String
        get() = plant.scientific_name
    override val imageUrl: String
        get() = plant.image_url
}

class PlantCommonName(
    private val plantCommonName: PlantCommonName
) : com.cramsan.petproject.appcore.storage.PlantCommonName {
    override val id: Long
        get() = plantCommonName.id
    override val commonName: String
        get() = plantCommonName.common_name
    override val plantId: Long
        get() = plantCommonName.plant_id
    override val locale: String
        get() = plantCommonName.locale
}

class PlantFamily(
    private val plantFamily: PlantFamily
) : com.cramsan.petproject.appcore.storage.PlantFamily {
    override val id: Long
        get() = plantFamily.id
    override val family: String
        get() = plantFamily.family
    override val plantId: Long
        get() = plantFamily.plant_id
    override val locale: String
        get() = plantFamily.locale
}

class PlantMainName(
    private val plantMainName: PlantMainName
) : com.cramsan.petproject.appcore.storage.PlantMainName {
    override val id: Long
        get() = plantMainName.id
    override val mainName: String
        get() = plantMainName.main_name
    override val plantId: Long
        get() = plantMainName.plant_id
    override val locale: String
        get() = plantMainName.locale
}

class Toxicity(
    private val toxicity: Toxicity
) : com.cramsan.petproject.appcore.storage.Toxicity {
    override val id: Long
        get() = toxicity.id
    override val plantId: Long
        get() = toxicity.plant_id
    override val animalId: AnimalType
        get() = toxicity.animal_id
    override val source: String
        get() = toxicity.source
    override val toxic: ToxicityValue
        get() = toxicity.is_toxic
}

class GetAllPlantsWithAnimalId : com.cramsan.petproject.appcore.storage.GetAllPlantsWithAnimalId {
    private val getAllPlantsWithAnimalId: GetAllPlantsWithAnimalId

    constructor(getAllPlantsWithAnimalId: GetAllPlantsWithAnimalId) {
        this.getAllPlantsWithAnimalId = getAllPlantsWithAnimalId
    }

    constructor(getAllPlantsWithAnimalIdAll: GetAllPlantsWithAnimalIdAll) {
        this.getAllPlantsWithAnimalId = GetAllPlantsWithAnimalId(
            getAllPlantsWithAnimalIdAll.id,
            getAllPlantsWithAnimalIdAll.scientific_name,
            getAllPlantsWithAnimalIdAll.main_name,
            getAllPlantsWithAnimalIdAll.animal_id,
            getAllPlantsWithAnimalIdAll.is_toxic
        )
    }

    override val id: Long
        get() = getAllPlantsWithAnimalId.id
    override val scientificName: String
        get() = getAllPlantsWithAnimalId.scientific_name
    override val mainName: String
        get() = getAllPlantsWithAnimalId.main_name
    override val animalId: AnimalType?
        get() = getAllPlantsWithAnimalId.animal_id
    override val isToxic: ToxicityValue?
        get() = getAllPlantsWithAnimalId.is_toxic
}

class GetPlantWithPlantIdAndAnimalId(
    private val getPlantWithPlantIdAndAnimalId: GetPlantWithPlantIdAndAnimalId
) : com.cramsan.petproject.appcore.storage.GetPlantWithPlantIdAndAnimalId {
    override val id: Long
        get() = getPlantWithPlantIdAndAnimalId.id
    override val scientificName: String
        get() = getPlantWithPlantIdAndAnimalId.scientific_name
    override val mainName: String
        get() = getPlantWithPlantIdAndAnimalId.main_name
    override val family: String
        get() = getPlantWithPlantIdAndAnimalId.family
    override val imageUrl: String
        get() = getPlantWithPlantIdAndAnimalId.image_url
    override val animalId: AnimalType
        get() = getPlantWithPlantIdAndAnimalId.animal_id
    override val commonNames: String
        get() = getPlantWithPlantIdAndAnimalId.common_names
    override val isToxic: ToxicityValue
        get() = getPlantWithPlantIdAndAnimalId.is_toxic
    override val description: String
        get() = getPlantWithPlantIdAndAnimalId.description
    override val source: String
        get() = getPlantWithPlantIdAndAnimalId.source
}
