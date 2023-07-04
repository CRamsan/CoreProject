package com.cramsan.ps2link.service.repository.mongo.models

import com.cramsan.ps2link.service.api.models.Faction
import com.cramsan.ps2link.service.api.models.Namespace
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

/**
 * Model class for the news articles
 */
@Serializable
data class Character(
    @BsonId
    val bsonId: String,
    var characterId: String,
    var namespace: Namespace,
    var name: String? = null,
    var faction: Faction? = null,
    var headId: Int? = null,
    var titleId: Int? = null,
    var times: Times? = null,
    var certs: Certs? = null,
    var battleRank: BattleRank? = null,
    var profileId: Int? = null,
    var dailyRibbon: DailyRibbon? = null,
    var lastUpdated: Long = Long.MIN_VALUE,
)
