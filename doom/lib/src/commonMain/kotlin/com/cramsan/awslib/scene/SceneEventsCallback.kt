package com.cramsan.awslib.scene

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.map.Cell

interface SceneEventsCallback {
    fun onItemChanged(entity: ItemInterface)
    fun onCharacterChanged(entity: CharacterInterface)
    fun onCellChanged(cell: Cell)
    fun onSceneEnded(completed: Boolean)
}
