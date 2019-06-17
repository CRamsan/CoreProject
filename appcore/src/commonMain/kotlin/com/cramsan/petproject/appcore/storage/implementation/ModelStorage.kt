package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.framework.CoreFramework
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Mapper
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.Toxicity
import com.cramsan.petproject.appcore.storage.ModelStorageInterface

internal class ModelStorage(private val initializer: ModelStorageInitializer) : ModelStorageInterface {
    var plantList = listOf<Plant>()

    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        val mapper = Mapper(mapOf(AnimalType.CAT to Toxicity(true, "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve")))
        val plant1 = Plant("Arum maculatum", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant2 = Plant("Arum maculatum 2", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant3 = Plant("Arum maculatum 3", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant4 = Plant("Arum maculatum 4", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        plantList = arrayListOf(plant1, plant2, plant3, plant4)
        CoreFramework.threadUtil.threadSleep(5)
        return plantList
    }

    override fun getPlant(uniqueName: String): Plant? {
        plantList.forEach {
            if (it.exactName != uniqueName)
                return@forEach

            return it
        }
        return null
    }

    override fun getItems(forceUpdate: Boolean) {

    }
}

