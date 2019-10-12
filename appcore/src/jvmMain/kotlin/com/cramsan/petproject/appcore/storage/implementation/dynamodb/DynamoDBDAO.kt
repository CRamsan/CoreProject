package com.cramsan.petproject.appcore.storage.implementation.dynamodb

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.GetAllPlantsWithAnimalId
import com.cramsan.petproject.appcore.storage.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

class DynamoDBDAO : ModelStorageDAO {

    val ddb: DynamoDbClient? = null

    override fun insertPlantEntry(plantId: Long?, scientificName: String, imageUrl: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPlantCommonNameEntry(commonNameId: Long?, commonName: String, plantId: Long, locale: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPlantMainNameEntry(mainNameId: Long?, mainName: String, plantId: Long, locale: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPlantFamilyNameEntry(familyId: Long?, family: String, plantId: Long, locale: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun insertToxicityEntry(
        toxicityId: Long?,
        isToxic: ToxicityValue,
        plantId: Long,
        animalType: AnimalType,
        source: String
    ) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun insertDescriptionEntry(
        descriptionId: Long?,
        plantId: Long,
        animalType: AnimalType,
        description: String,
        locale: String
    ) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlantEntry(scientificName: String): Plant? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPlantEntries(): List<Plant> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPlantCommonNameEntries(): List<PlantCommonName> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPlantMainNameEntries(): List<PlantMainName> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPlantFamilyEntries(): List<PlantFamily> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllToxicityEntries(): List<Toxicity> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllDescriptionEntries(): List<Description> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getCustomPlantEntry(
        plantId: Long,
        animalType: AnimalType,
        locale: String
    ): GetPlantWithPlantIdAndAnimalId? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
