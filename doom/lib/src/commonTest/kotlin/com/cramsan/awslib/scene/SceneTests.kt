package com.cramsan.awslib.scene

import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.platform.runTest
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import io.mockk.mockk
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SceneTests {

    lateinit var kodein: DI

    @BeforeTest
    fun prepareTest() {
        val log = mockk<EventLoggerInterface>(relaxed = true)
        val assert = mockk<AssertUtilInterface>(relaxed = true)
        val halt = mockk<HaltUtilInterface>(relaxed = true)
        kodein = DI {
            bind() from singleton { log }
            bind() from singleton { assert }
            bind() from singleton { halt }
        }
    }

    /**
     * Test Basic Scene
     */
    @Test
    fun basicSceneTest() = runTest {
        val map = GameMap(MapGenerator.createMap100x100())
        val sceneConfig = scene {
            player {
                posX = 25
                posY = 25
            }
            entities {
                dog {
                    id = 1
                    posX = 2
                    posY = 2
                }
                dog {
                    id = 2
                    posX = 4
                    posY = 39
                }
                dog {
                    id = 3
                    posX = 58
                    posY = 90
                }
                dog {
                    id = 4
                    posX = 92
                    posY = 83
                }
                dog {
                    id = 5
                    posX = 50
                    posY = 18
                }
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, kodein)

        val player = sceneConfig.player

        val actionListSouth = Array(20) { TurnAction(TurnActionType.MOVE, Direction.SOUTH) }
        val actionListNorth = Array(20) { TurnAction(TurnActionType.MOVE, Direction.NORTH) }
        val actionListWest = Array(20) { TurnAction(TurnActionType.MOVE, Direction.WEST) }
        val actionListEast = Array(20) { TurnAction(TurnActionType.MOVE, Direction.EAST) }

        val scene = Scene(entityManager, sceneConfig, kodein)
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
    fun mapWithWallsSceneTest() = runTest {
        val map = GameMap(MapGenerator.createMapWithWalls())

        val sceneConfig = scene {
            player {
                posX = 1
                posY = 1
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, kodein)

        val player = sceneConfig.player
        val actionListSouth = Array(15) { TurnAction(TurnActionType.MOVE, Direction.SOUTH) }
        val actionListNorth = Array(15) { TurnAction(TurnActionType.MOVE, Direction.NORTH) }
        val actionListWest = Array(7) { TurnAction(TurnActionType.MOVE, Direction.WEST) }
        val actionListEast = Array(7) { TurnAction(TurnActionType.MOVE, Direction.EAST) }

        val scene = Scene(entityManager, sceneConfig, kodein)
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

    /**
     * Test Basic Scene
     */
    @Test
    fun testDuplicateIds() {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 0
                posY = 25
            }
            entities {
                dog {
                    id = 1
                    posX = 2
                    posY = 2
                }
            }
        }
        assertNotNull(sceneConfig)

        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, kodein)

        val scene = Scene(entityManager, sceneConfig, kodein)
        try {
            scene.loadScene()
        } catch (e: Exception) {
            assertEquals(e.message?.startsWith("Could not register dog "), true)
        }
    }
}
