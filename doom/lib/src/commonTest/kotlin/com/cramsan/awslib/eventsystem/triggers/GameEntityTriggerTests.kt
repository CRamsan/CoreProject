package com.cramsan.awslib.eventsystem.triggers

import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.platform.runTest
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameEntityTriggerTests {

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
                entity {
                    id = 523
                    eventId = 352
                    targetId = 1
                    enabled = true
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
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, null, kodein)
        val player = sceneConfig.player

        val scene = Scene(entityManager, sceneConfig, kodein)
        scene.loadScene()

        player.heading = Direction.SOUTH
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)
        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        val enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.type, EntityType.DOG)
    }

    /**
     * Test GameEntityTrigger with Options
     */
    @Test
    fun gameEntityTriggerWithInteractiveEvent() = runTest {
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
                entity {
                    id = 523
                    eventId = 912
                    targetId = 1
                    enabled = true
                }
            }
            events {
                interactive {
                    id = 912
                    text = "Should I transform?"
                    option {
                        id = 0
                        eventId = 352
                        label = "Yes"
                    }
                    option {
                        id = 1
                        eventId = InitialValues.NOOP_ID
                        label = "No"
                    }
                }
                swapEntity {
                    id = 352
                    enableEntityId = 2
                    disableEntityId = 1
                    nextEventId = 0
                }
            }
        }

        assertNotNull(sceneConfig)
        var targetIndex = 1
        val entityManager = EntityManager(
            map,
            sceneConfig.triggerList,
            sceneConfig.eventList,
            object : EntityManagerEventListener {
                override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onInteractionRequired(text: String, options: List<InteractiveEventOption>, eventReceiver: EntityManagerInteractionReceiver) {
                    GlobalScope.launch {
                        options.forEach {
                            println("EventID: ${it.eventId} - ${it.id} - ${it.label}")
                        }
                        eventReceiver.selectOption(options[targetIndex])
                    }
                }
            },
            kodein
        )
        val player = sceneConfig.player
        val scene = Scene(entityManager, sceneConfig, kodein)

        scene.loadScene()
        player.heading = Direction.SOUTH
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)

        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        var enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.type, EntityType.SCIENTIST)

        targetIndex = 0
        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.type, EntityType.DOG)
    }

    /**
     * Test GameEntityTrigger without Options
     */
    @Test
    fun gameEntityTriggerWithInteractiveEventWithoutOptions() = runTest {
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
            }
            triggers {
                entity {
                    id = 523
                    eventId = 912
                    targetId = 1
                    enabled = true
                }
            }
            events {
                interactive {
                    id = 912
                    text = "I am happy to see you"
                }
            }
        }

        assertNotNull(sceneConfig)
        val entityManager = EntityManager(
            map,
            sceneConfig.triggerList,
            sceneConfig.eventList,
            object : EntityManagerEventListener {
                override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onInteractionRequired(text: String, options: List<InteractiveEventOption>, eventReceiver: EntityManagerInteractionReceiver) {
                    runTest {
                        assertEquals(0, options.size)
                        eventReceiver.selectOption(null)
                    }
                }
            },
            kodein
        )
        val player = sceneConfig.player
        val scene = Scene(entityManager, sceneConfig, kodein)

        scene.loadScene()
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)

        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        var scientist = entityManager.getEntity(5, 6)
        assertNotNull(scientist)
        assertEquals(scientist.type, EntityType.SCIENTIST)
    }
}
