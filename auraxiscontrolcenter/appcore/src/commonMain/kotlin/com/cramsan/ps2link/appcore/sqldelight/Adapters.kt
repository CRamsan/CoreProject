package com.cramsan.ps2link.appcore.sqldelight

import com.cramsan.ps2link.db.models.CharacterClass
import com.cramsan.ps2link.db.models.Faction
import com.cramsan.ps2link.db.models.LoginStatus
import com.cramsan.ps2link.db.models.Namespace
import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

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

val instantAdapter = object : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long) = Instant.fromEpochMilliseconds(databaseValue)
    override fun encode(value: Instant) = value.toEpochMilliseconds()
}

@OptIn(ExperimentalTime::class)
val durationAdapter = object : ColumnAdapter<Duration, Long> {
    override fun decode(databaseValue: Long) = databaseValue.milliseconds
    override fun encode(value: Duration) = value.inWholeMilliseconds
}

val characterClassAdapter = object : ColumnAdapter<CharacterClass, Long> {
    override fun decode(databaseValue: Long) = CharacterClass.fromLong(databaseValue)
    override fun encode(value: CharacterClass) = value.id
}
