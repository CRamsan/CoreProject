package com.cramsan.petproject.model

open class Item(val exactName: String,
                val commonName: List<String>,
                val toxicityMapper: Mapper
)