package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.db.models.LoginStatus
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

val namespaceAdapter = object : ColumnAdapter<Namespace, String> {
    override fun decode(databaseValue: String) = Namespace.fromString(databaseValue)
    override fun encode(value: Namespace) = value.namespace
}

val loginStatusAdapter = object : ColumnAdapter<LoginStatus, String> {
    override fun decode(databaseValue: String) = LoginStatus.fromString(databaseValue)
    override fun encode(value: LoginStatus) = value.code
}
