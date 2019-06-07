package com.cramsan.petproject.storage.implementation

import com.cramsan.petproject.framework.CoreFramework
import com.cramsan.petproject.logging.implementation.getTag
import com.cramsan.petproject.storage.ModelStorageInterface
import com.cramsan.petproject.storage.ModelStorageListenerInterface

class ModelStorage : ModelStorageInterface {

    val listenerSet = mutableSetOf<ModelStorageListenerInterface>()

    override fun registerListener(listener: ModelStorageListenerInterface) {
        CoreFramework.eventLogger.assert(listenerSet.add(listener), getTag(), "Listener was already registered")
    }

    override fun deregisterListener(listener: ModelStorageListenerInterface) {
        CoreFramework.eventLogger.assert(listenerSet.remove(listener), getTag(), "Listener was not removed")
    }

    override fun getPlants(forceUpdate: Boolean) {
    }

    override fun getItems(forceUpdate: Boolean) {
    }
}