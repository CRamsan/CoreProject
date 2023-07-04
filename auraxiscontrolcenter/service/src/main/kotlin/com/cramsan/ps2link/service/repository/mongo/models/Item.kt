package com.cramsan.ps2link.service.repository.mongo.models

import com.cramsan.ps2link.service.api.models.Faction
import com.cramsan.ps2link.service.api.models.Namespace
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Item(
    @BsonId
    val bsonId: String,
    val itemId: String,
    val namespace: Namespace,
    val itemTypeId: String? = null,
    val itemCategoryId: String? = null,
    val isVehicleWeapon: String? = null,
    val name: String? = null,
    val description: String? = null,
    val faction: Faction? = null,
    val maxStackSize: String? = null,
    val imageSetId: String? = null,
    val imageId: String? = null,
    val imagePath: String? = null,
    val isDefaultAttachment: String? = null,
    var lastUpdated: Long = Long.MIN_VALUE,
)
