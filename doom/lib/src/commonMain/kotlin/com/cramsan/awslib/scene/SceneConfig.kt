package com.cramsan.awslib.scene

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.triggers.Trigger

data class SceneConfig(
    val player: Player,
    val characterList: List<CharacterInterface>,
    val itemList: List<ItemInterface>,
    val triggerList: List<Trigger>,
    val eventList: List<BaseEvent>
)
