package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.framework.CoreFramework
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.db.Animal
import com.cramsan.petproject.db.PetProjectDB

internal class ModelStorage(initializer: ModelStorageInitializer) : ModelStorageInterface {
    var plantList = listOf<Plant>()

    var database: PetProjectDB

    init {
        val sqlDriver = initializer.platformInitializer.getSqlDriver()
        database = PetProjectDB(sqlDriver,
            AnimalAdapter = Animal.Adapter(CommonNameAdapter())
        )
        CoreFramework.threadUtil.dispatchToBackground {
            if (database.animalQueries.getAnimal(AnimalType.DOG).executeAsOneOrNull() == null) {
                database.animalQueries.insert(AnimalType.CAT)
                database.animalQueries.insert(AnimalType.DOG)
                val dog = database.animalQueries.getAnimal(AnimalType.DOG).executeAsOne()
                val cat = database.animalQueries.getAnimal(AnimalType.CAT).executeAsOne()
                database.plantQueries.insert(
                    "Arum maculatum",
                    "Araceae",
                    "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
                )
                val newPlant = database.plantQueries.getPlant("Arum maculatum").executeAsOne()
                database.plantCommonNameQueries.insert("Arum", newPlant.id)
                database.plantCommonNameQueries.insert("Lord-and-Ladies", newPlant.id)
                database.plantCommonNameQueries.insert("Wake Robin", newPlant.id)
                database.toxicityQueries.insert(
                    newPlant.id,
                    dog.id,
                    true,
                    "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
                )
                database.toxicityQueries.insert(
                    newPlant.id,
                    cat.id,
                    false,
                    "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
                )
            }
        }
    }

    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        CoreFramework.threadUtil.assertIsBackgroundThread()
        //val list = database.plantQueries.getAll().executeAsList()
        val list = database.customProjectionsQueries.getAllPlants().executeAsList()
        val mutableList = mutableListOf<Plant>()
        list.forEach {
            mutableList.add(
                Plant(it.id.toInt(),
                    it.scientific_name,
                    it.common_names,
                    it.image_url,
                    it.family,
                    null
                ))
        }
        plantList = mutableList
        return plantList
    }

    override fun getPlant(plantId: Int): Plant? {
        CoreFramework.threadUtil.assertIsBackgroundThread()
        plantList.forEach {
            if (it.id != plantId)
                return@forEach

            return it
        }
        return null
    }

    override fun getItems(forceUpdate: Boolean) {
        CoreFramework.threadUtil.assertIsBackgroundThread()
    }
}

