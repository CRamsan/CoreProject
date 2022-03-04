package com.cramsan.stranded.web.game.models

import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Food
import com.cramsan.stranded.lib.game.models.common.Weapon
import com.cramsan.stranded.lib.game.models.crafting.Craftable

sealed class MessageElement {

    data class Normal(val text: String) : MessageElement()

    data class Food(val card: Food) : MessageElement()

    data class Weapon(val card: Weapon) : MessageElement()

    data class Craftable(val card: Craftable) : MessageElement()

    data class Action(val elements: List<MessageElement>) : MessageElement()

}
