package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.model.Toxicity
import com.cramsan.petproject.appcore.storage.ModelStorageInterface

class ModelStorage(initializer: ModelStorageInitializer) : ModelStorageInterface {

    private var sqlDelightDAO: SQLDelightDAO = SQLDelightDAO(initializer)

    override fun getPlants(animalType: AnimalType, forceUpdate: Boolean): List<Plant> {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()

        val tlist = sqlDelightDAO.getCustomPlantEntries()
        if (tlist.isEmpty()) {
            test()
            insertMorePlants()
        }
        val animalEntry = sqlDelightDAO.getAnimalEntry(animalType)
        val list = sqlDelightDAO.getCustomPlantEntries(animalEntry.id)
        val mutableList = mutableListOf<Plant>()

        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientific_name,
                    it.main_common_name,
                    it.common_names,
                    it.image_url,
                    it.family,
                    it.is_toxic
                ))
        }
        return mutableList
    }

    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()

        val list = sqlDelightDAO.getCustomPlantEntries()

        val mutableList = mutableListOf<Plant>()
        list.forEach {
            mutableList.add(
                Plant(
                    it.id.toInt(),
                    it.scientific_name,
                    it.main_common_name,
                    it.common_names,
                    it.image_url,
                    it.family,
                    null
                ))
        }
        return mutableList
    }

    override fun getPlant(plantId: Int): Plant? {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()
        val plantList = getPlants(true)
        plantList.forEach {
            if (it.id != plantId)
                return@forEach

            return it
        }
        return null
    }

    override fun getToxicity(animalType: AnimalType, plantId: Int) : Toxicity {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()

        val animalEntry = sqlDelightDAO.getAnimalEntry(animalType)
        val toxicityEntry = sqlDelightDAO.getToxicityEntry(plantId.toLong(), animalEntry.id)

        return Toxicity(toxicityEntry.is_toxic, toxicityEntry.source)
    }

    override fun getPlantMetadata(animalType: AnimalType, plantId: Int) : PlantMetadata {
        val animalEntry = sqlDelightDAO.getAnimalEntry(animalType)
        return PlantMetadata(0, plantId, AnimalType.CAT, true, "This asd" +
                "a sdas dasdasdasd wq da s da  eqwew dwad " +
                "a ddasdasdasd wq da s da  eqwew dwad " +
                "as asd sa dasdasdasd wq da s da  eqwew dwad " +
                " dasdasdasd wq da s da  eqwew dwad as  sa" +
                " dasdasdasd wq da s da  eqwew dwad ", "https://www.google.com")
    }

    fun test() {
        sqlDelightDAO.insertAnimalEntry(AnimalType.CAT)
        sqlDelightDAO.insertAnimalEntry(AnimalType.DOG)

        val dog = sqlDelightDAO.getAnimalEntry(AnimalType.DOG)
        val cat = sqlDelightDAO.getAnimalEntry(AnimalType.CAT)

        sqlDelightDAO.insertPlantEntry("Arum maculatum",
            "Adam and Eve",
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
                "Arum maculatum $i",
                "Adam and Eve $i",
                "Araceae",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
            )
            val new = sqlDelightDAO.getPlantEntry("Arum maculatum $i")
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