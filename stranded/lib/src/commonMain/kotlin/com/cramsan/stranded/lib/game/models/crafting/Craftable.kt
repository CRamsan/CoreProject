package com.cramsan.stranded.lib.game.models.crafting

import com.cramsan.stranded.lib.game.models.common.Card
import kotlinx.serialization.Serializable

/**
 * This is the parent class to all cards that can be crafted.
 */
@Serializable
sealed class Craftable(val _title: String) : Card(title = _title)
