package com.cramsan.petproject.framework.storage.relational.implementation

import com.cramsan.petproject.framework.storage.relational.RelationalInterface

actual class Relational actual constructor(databaseName: String) : RelationalInterface {
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}