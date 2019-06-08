package com.cramsan.petproject.storage.implementation

import com.cramsan.petproject.framework.CoreFramework
import com.cramsan.petproject.logging.implementation.getTag
import com.cramsan.petproject.model.AnimalType
import com.cramsan.petproject.model.Mapper
import com.cramsan.petproject.model.Plant
import com.cramsan.petproject.model.Toxicity
import com.cramsan.petproject.storage.ModelStorageInterface
import com.cramsan.petproject.storage.ModelStorageListenerInterface

internal class ModelStorage : ModelStorageInterface {

    val listenerSet = mutableSetOf<ModelStorageListenerInterface>()
    var plantList = listOf<Plant>()

    override fun registerListener(listener: ModelStorageListenerInterface) {
        CoreFramework.eventLogger.assert(listenerSet.add(listener), getTag(), "Listener was already registered")
    }

    override fun deregisterListener(listener: ModelStorageListenerInterface) {
        CoreFramework.eventLogger.assert(listenerSet.remove(listener), getTag(), "Listener was not removed")
    }

    override fun getPlants(forceUpdate: Boolean) {
        val mapper = Mapper(mapOf(AnimalType.CAT to Toxicity(true, "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve")))
        val plant1 = Plant("Arum maculatum", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant2 = Plant("Arum maculatum 2", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant3 = Plant("Arum maculatum 3", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        val plant4 = Plant("Arum maculatum 4", listOf("Arum", "Lord-and-Ladies", "Wake Robin", "Starch Root", "Bobbins", "Cuckoo Plant"), "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ","Araceae", mapper)
        plantList = arrayListOf(plant1, plant2, plant3, plant4);
        listenerSet.forEach {
            it.onUpdate(plantList)
        }
    }

    override fun getPlant(uniqueName: String) {
        plantList.forEach { plant ->
            if (plant.exactName != uniqueName)
                return@forEach

            listenerSet.forEach {
                it.onUpdate(plant)
            }
            return
        }
    }

    override fun getItems(forceUpdate: Boolean) {
    }
}