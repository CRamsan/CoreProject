package com.cramsan.ps2link.service

import com.cramsan.ps2link.network.models.content.CharacterProfile
import com.cramsan.ps2link.service.api.models.Faction
import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.repository.mongo.models.BattleRank
import com.cramsan.ps2link.service.repository.mongo.models.Certs
import com.cramsan.ps2link.service.repository.mongo.models.Character
import com.cramsan.ps2link.service.repository.mongo.models.Item
import com.cramsan.ps2link.service.repository.mongo.models.Times
import org.bson.types.ObjectId
import com.cramsan.ps2link.network.models.content.item.Item as ItemResponse

fun String.toNamespace(): Namespace? {
    return when (this) {
        com.cramsan.ps2link.network.models.Namespace.PS2PC.namespace -> Namespace.PS2PC
        com.cramsan.ps2link.network.models.Namespace.PS2PS4US.namespace -> Namespace.PS2PS4US
        com.cramsan.ps2link.network.models.Namespace.PS2PS4EU.namespace -> Namespace.PS2PS4EU
        else -> null
    }
}

fun Namespace.toCensusModel(): com.cramsan.ps2link.network.models.Namespace {
    return when (this) {
        Namespace.PS2PC -> com.cramsan.ps2link.network.models.Namespace.PS2PC
        Namespace.PS2PS4US -> com.cramsan.ps2link.network.models.Namespace.PS2PS4US
        Namespace.PS2PS4EU -> com.cramsan.ps2link.network.models.Namespace.PS2PS4EU
    }
}

fun CharacterProfile.toEntity(namespace: Namespace): Character {
    return Character(
        bsonId = "$character_id-$namespace",
        characterId = character_id,
        namespace = namespace,
        name = name?.first ?: "",
        faction = Faction.fromString(faction_id),
        headId = head_id?.toIntOrNull(),
        titleId = title_id?.toIntOrNull(),
        times = Times(
            lastLogin = times?.last_login?.toLongOrNull(),
            minutesPlayed = times?.minutes_played?.toLongOrNull(),
            creation = times?.creation?.toLongOrNull(),
            loginCount = times?.login_count?.toLongOrNull(),
        ),
        certs = Certs(
            availablePoints = certs?.available_points?.toIntOrNull(),
            earnedPoints = certs?.earned_points?.toIntOrNull(),
            giftedPoints = certs?.gifted_points?.toIntOrNull(),
            spentPoints = certs?.spent_points?.toIntOrNull(),
            percentToNext = certs?.percent_to_next?.toFloatOrNull(),
        ),
        battleRank = BattleRank(
            percentToNnext = battle_rank?.percent_to_next?.let {
                it / 100f
            },
            value = battle_rank?.value,
        ),
        profileId = profile_id?.toIntOrNull(),
        dailyRibbon = null,
        lastUpdated = Long.MIN_VALUE,
    )
}


fun ItemResponse.toEntity(namespace: Namespace): Item {
    return Item(
        bsonId = "$item_id-$namespace",
        itemId = item_id,
        namespace = namespace,
        itemTypeId = item_type_id,
        itemCategoryId = item_category_id,
        isVehicleWeapon = is_vehicle_weapon,
        name = name?.en,
        description = description?.en,
        faction = Faction.fromString(faction_id),
        maxStackSize = max_stack_size,
        imageSetId = image_set_id,
        imageId = image_id,
        imagePath = image_path,
        isDefaultAttachment = is_default_attachment,
        lastUpdated = Long.MIN_VALUE,
    )
}

fun Faction.Companion.fromString(factionId: String?): Faction? {
    return when (factionId) {
        "1" -> Faction.VS
        "2" -> Faction.NC
        "3" -> Faction.TR
        "4" -> Faction.NS
        else -> null
    }
}
