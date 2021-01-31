package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.db.models.Namespace
import com.squareup.sqldelight.ColumnAdapter

/**
 * @Author cramsan
 * @created 1/17/2021
 */

val factionAdapter = object : ColumnAdapter<Faction, String> {
    override fun decode(databaseValue: String) = Faction.fromString(databaseValue)
    override fun encode(value: Faction) = value.code
}

fun Faction.toSqlType() = factionAdapter.encode(this)

val namespaceAdapter = object : ColumnAdapter<Namespace, String> {
    override fun decode(databaseValue: String) = Namespace.fromString(databaseValue)
    override fun encode(value: Namespace) = value.namespace
}

fun Namespace.toSqlType() = namespaceAdapter.encode(this)
