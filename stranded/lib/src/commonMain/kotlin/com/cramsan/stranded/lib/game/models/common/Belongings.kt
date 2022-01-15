package com.cramsan.stranded.lib.game.models.common

import kotlinx.serialization.Serializable

/**
 * Class that represents a card that the player starts with.
 */
@Serializable
sealed class Belongings(val _title: String) : Card(title = _title), UsableItem
