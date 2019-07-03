package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.implementation.getTag
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.storage.ModelStorageInterface

class ModelStorage(initializer: ModelStorageInitializer) : ModelStorageInterface {
    private var plantList: List<Plant>? = null

    private var sqlDelightDAO: SQLDelightDAO = SQLDelightDAO(initializer)

    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()
        val cachedPlantList = plantList
        if (!forceUpdate) {
            if (cachedPlantList != null) {
                return cachedPlantList
            }
        }

        val list = sqlDelightDAO.getCustomPlantEntries()

        if (list.isEmpty()) {
            test()
            insertMorePlants()
        }

        val mutableList = mutableListOf<Plant>()
        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientific_name,
                    it.common_names,
                    it.image_url,
                    it.family,
                    null
                ))
        }
        plantList = mutableList
        return mutableList
    }

    override fun getPlant(plantId: Int): Plant? {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()
        val cachedPlantList = plantList
        if (cachedPlantList == null) {
            getPlants(true)
        }

        CoreFrameworkAPI.eventLogger.assert(plantList != null, getTag(), "Plant list should always" +
                "be loaded after calling getPlants()")
        plantList?.forEach {
            if (it.id != plantId)
                return@forEach

            return it
        }
        return null
    }

    fun test() {
        sqlDelightDAO.insertAnimalEntry(AnimalType.CAT)
        sqlDelightDAO.insertAnimalEntry(AnimalType.DOG)

        val dog = sqlDelightDAO.getAnimalEntry(AnimalType.DOG)
        val cat = sqlDelightDAO.getAnimalEntry(AnimalType.CAT)

        sqlDelightDAO.insertPlantEntry("Arum maculatum",
            "Araceae",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
        )
        val newPlant = sqlDelightDAO.getPlantEntry("Arum maculatum")
        sqlDelightDAO.insertPlantCommonNameEntry("Arum", newPlant.id)
        sqlDelightDAO.insertPlantCommonNameEntry("Lord-and-Ladies", newPlant.id)
        sqlDelightDAO.insertPlantCommonNameEntry("Wake Robin", newPlant.id)
        sqlDelightDAO.insertToxicityEntry(true,
            newPlant.id,
            dog.id,
            "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
        )
        sqlDelightDAO.insertToxicityEntry(false,
            newPlant.id,
            cat.id,
            "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
        )
    }

    fun insertMorePlants() {
        for (i in 1..100) {
            sqlDelightDAO.insertPlantEntry(
                "Arum maculatum$i",
                "Araceae",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
            )
            val new = sqlDelightDAO.getPlantEntry("Arum maculatum$i")
            sqlDelightDAO.insertPlantCommonNameEntry("Arum", new.id)
            sqlDelightDAO.insertToxicityEntry(true,
                new.id,
                1,
                "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
            )
        }
    }


    fun deleteAll() {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()
        sqlDelightDAO.deleteAll()
    }
}