package com.cramsan.awslib.scene

import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.map.Cell

interface SceneEventsCallback {
    fun onEntityChanged(entity: GameEntityInterface)
    fun onCellChanged(cell: Cell)
    fun onSceneEnded(completed: Boolean)
}
