package com.cramsan.petproject.storage.provider.implementation

import com.cramsan.petproject.model.Plant
import com.cramsan.petproject.storage.provider.ModelStorageInterface
import com.cramsan.petproject.storage.provider.ModelStorageListenerInterface

class ModelStorage : ModelStorageInterface {

    val listenerSet = mutableSetOf<ModelStorageListenerInterface>()

    override fun registerListener(listener: ModelStorageListenerInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deregisterListener(listener: ModelStorageListenerInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}