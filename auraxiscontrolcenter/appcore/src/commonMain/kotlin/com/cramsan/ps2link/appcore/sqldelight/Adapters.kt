package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.appcore.dbg.Faction
import com.squareup.sqldelight.ColumnAdapter

/**
 * @Author cramsan
 * @created 1/17/2021
 */

val factionAdapter = object : ColumnAdapter<Faction, String> {
    override fun decode(databaseValue: String) = Faction.fromString(databaseValue)
    override fun encode(value: Faction) = value.code
}
