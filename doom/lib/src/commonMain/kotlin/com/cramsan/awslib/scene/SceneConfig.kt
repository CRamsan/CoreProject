package com.cramsan.awslib.scene

import com.cramsan.awslib.entity.implementation.GameEntity
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.triggers.Trigger

data class SceneConfig(
    val player: Player,
    val entityList: List<GameEntity>,
    val triggerList: List<Trigger>,
    val eventList: List<BaseEvent>
)