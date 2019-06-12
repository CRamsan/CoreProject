package com.cramsan.petproject.storage.sqlite.implementation

import com.cramsan.petproject.model.Plant
import com.cramsan.petproject.storage.sqlite.DAOInterface

actual class DAO : DAOInterface {
    override fun getPlants(forceUpdate: Boolean): List<Plant> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlant(uniqueName: String): Plant? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(forceUpdate: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}