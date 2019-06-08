package com.cramsan.petproject.model

open class Item(val exactName: String,
                val commonName: List<String>,
                val imageUrl: String,
                val toxicityMapper: Mapper
)