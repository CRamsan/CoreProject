package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.implementation.getTag
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.db.Animal
import com.cramsan.petproject.db.PetProjectDB
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ModelStorage(initializer: ModelStorageInitializer) : ModelStorageInterface {
    private var plantList: List<Plant>? = null

    private var database: PetProjectDB
    private val mutex = Mutex()

    init {
        val sqlDriver = initializer.platformInitializer.getSqlDriver()
        database = PetProjectDB(sqlDriver,
            AnimalAdapter = Animal.Adapter(CommonNameAdapter())
        )
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

    override suspend fun getPlants(forceUpdate: Boolean): List<Plant> {
        CoreFrameworkAPI.threadUtil.assertIsBackgroundThread()
        val cachedPlantList = plantList
        if (!forceUpdate) {
            if (cachedPlantList != null) {
                return cachedPlantList
            }
        }

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
        return mutableList
    }

    override suspend fun getPlant(plantId: Int): Plant? {
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
}

