package com.cramsan.awslib.eventsystem.triggers

import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.platform.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CellTriggerTests {

    /**
     * Test GameEntityTrigger
     */
    @Test
    fun gameEntityTriggerWithSwapEventTest() = runTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 5
                posY = 5
            }
            entities {
                scientist {
                    id = 1
                    group = 0
                    posX = 5
                    posY = 6
                }
                dog {
                    id = 2
                    posX = 4
                    posY = 9
                    enabled = false
                }
            }
            triggers {
                cell {
                    id = 523
                    eventId = 352
                    enabled = true
                    posX = 5
                    posY = 4
                }
            }
            events {
                swapEntity {
                    id = 352
                    enableEntityId = 2
                    disableEntityId = 1
                    nextEventId = 0
                }
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, null)
        val player = sceneConfig.player

        val scene = Scene(entityManager, sceneConfig)
        scene.loadScene()

        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.NORTH))
        assertEquals(5, player.posX)
        assertEquals(4, player.posY)
        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        val enemy = entityManager.getEntity(5, 5)
        assertNotNull(enemy)
        assertEquals(enemy.type, EntityType.DOG)
    }
}