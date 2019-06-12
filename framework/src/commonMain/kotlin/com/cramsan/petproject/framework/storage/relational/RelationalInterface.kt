package com.cramsan.petproject.framework.storage.relational

interface RelationalInterface {
    val name: String

    fun open()

    fun close()
}