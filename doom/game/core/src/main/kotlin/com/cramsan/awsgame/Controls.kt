package com.cramsan.awsgame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType

class Controls {

    var direction: Direction = Direction.KEEP
    var turnAction: TurnAction = TurnAction(TurnActionType.NONE, Direction.KEEP)

    fun update() {
        turnAction = TurnAction(TurnActionType.NONE, Direction.KEEP)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            turnAction = TurnAction(TurnActionType.MOVE, Direction.WEST)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            turnAction = TurnAction(TurnActionType.MOVE, Direction.EAST)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            turnAction = TurnAction(TurnActionType.MOVE, Direction.NORTH)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            turnAction = TurnAction(TurnActionType.MOVE, Direction.SOUTH)
        }
    }
}
