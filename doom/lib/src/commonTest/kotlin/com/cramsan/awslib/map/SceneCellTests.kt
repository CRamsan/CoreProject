package com.cramsan.awslib.map

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SceneCellTests : TestBase() {

    private lateinit var log: EventLoggerInterface
    private lateinit var assert: AssertUtilInterface
    private lateinit var halt: HaltUtil
    private lateinit var aiRepo: AIRepo

    @BeforeTest
    fun prepareTest() {
        log = mockk(relaxed = true)
        assert = mockk(relaxed = true)
        halt = mockk(relaxed = true)
        aiRepo = mockk()
    }

    /**
     * Test Basic Scene
     */
    @Test
    fun basicSceneTest() = runBlockingTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 25
                posY = 25
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, log, aiRepo)
        val player = sceneConfig.player

        val actionListSouth = Array(20) { TurnAction(TurnActionType.MOVE, Direction.SOUTH) }
        val actionListNorth = Array(20) { TurnAction(TurnActionType.MOVE, Direction.NORTH) }
        val actionListWest = Array(20) { TurnAction(TurnActionType.MOVE, Direction.WEST) }
        val actionListEast = Array(20) { TurnAction(TurnActionType.MOVE, Direction.EAST) }

        val scene = Scene(entityManager, sceneConfig, log)
        scene.loadScene()

        actionListSouth.forEach {
            scene.runTurn(it)
        }
        assertEquals(25, player.posX)
        assertEquals(45, player.posY)

        actionListEast.forEach {
            scene.runTurn(it)
        }
        assertEquals(45, player.posX)
        assertEquals(45, player.posY)

        actionListNorth.forEach {
            scene.runTurn(it)
        }
        assertEquals(45, player.posX)
        assertEquals(25, player.posY)

        actionListWest.forEach {
            scene.runTurn(it)
        }

        assertEquals(25, player.posX)
        assertEquals(25, player.posY)
    }

    /**
     * Test Basic Scene
     */
    @Test
    fun mapWithWallsSceneTest() = runBlockingTest {
        val map = GameMap(MapGenerator.createMapWithWalls())

        val sceneConfig = scene {
            player {
                posX = 1
                posY = 1
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, log, aiRepo)
        val player = sceneConfig.player

        val actionListSouth = Array(15) { TurnAction(TurnActionType.MOVE, Direction.SOUTH) }
        val actionListNorth = Array(15) { TurnAction(TurnActionType.MOVE, Direction.NORTH) }
        val actionListWest = Array(7) { TurnAction(TurnActionType.MOVE, Direction.WEST) }
        val actionListEast = Array(7) { TurnAction(TurnActionType.MOVE, Direction.EAST) }

        val scene = Scene(entityManager, sceneConfig, log)
        scene.loadScene()

        actionListSouth.forEach {
            scene.runTurn(it)
        }
        assertEquals(1, player.posX)
        assertEquals(16, player.posY)

        actionListEast.forEach {
            scene.runTurn(it)
        }
        assertEquals(7, player.posX)
        assertEquals(16, player.posY)

        actionListNorth.forEach {
            scene.runTurn(it)
        }
        assertEquals(7, player.posX)
        assertEquals(1, player.posY)

        actionListWest.forEach {
            scene.runTurn(it)
        }

        assertEquals(1, player.posX)
        assertEquals(1, player.posY)
    }
}
