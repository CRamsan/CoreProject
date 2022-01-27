package com.cramsan.awslib.eventsystem.triggers

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.AllyType
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameEntityTriggerTests : TestBase() {

    private lateinit var log: EventLoggerInterface
    private lateinit var assert: AssertUtilInterface
    private lateinit var halt: HaltUtil
    private lateinit var aiRepo: AIRepo

    override fun setupTest() {
        log = mockk(relaxed = true)
        EventLogger.setInstance(log)
        assert = mockk(relaxed = true)
        halt = mockk(relaxed = true)
        aiRepo = mockk()
    }

    /**
     * Test GameEntityTrigger
     */
    @Ignore
    @Test
    fun gameEntityTriggerWithSwapEventTest() = runBlockingTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 5
                posY = 5
            }
            entityBuilders {
                ally {
                    id = "1"
                }
                enemy {
                    id = "1"
                }
            }
            entity {
                ally {
                    id = "1"
                    template = "1"
                    group = "0"
                    posX = 5
                    posY = 6
                }
                enemy {
                    id = "2"
                    posX = 4
                    template = "1"
                    posY = 9
                    enabled = false
                }
            }
            triggers {
                character {
                    id = "523"
                    eventId = "352"
                    targetId = "1"
                    enabled = true
                }
            }
            events {
                swapCharacter {
                    id = "352"
                    enableCharacterId = "2"
                    disableCharacterId = "1"
                    nextEventId = InitialValues.NOOP_ID
                }
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, log, aiRepo)
        val player = sceneConfig.player

        val scene = Scene(entityManager, sceneConfig, log)
        scene.loadScene()

        player.heading = Direction.SOUTH
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)
        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        val enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.group, InitialValues.ENEMY_GROUP)
    }

    /**
     * Test GameEntityTrigger with Options
     */
    @OptIn(DelicateCoroutinesApi::class)
    @Ignore
    @Test
    fun gameEntityTriggerWithInteractiveEvent() = runBlockingTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 5
                posY = 5
            }
            entityBuilders {
                ally {
                    id = "1"
                }
                enemy {
                    id = "1"
                }
            }
            entity {
                ally {
                    id = "1"
                    template = "1"
                    posX = 5
                    posY = 6
                }
                enemy {
                    id = "2"
                    template = "1"
                    posX = 4
                    posY = 9
                    enabled = false
                }
            }
            triggers {
                character {
                    id = "523"
                    eventId = "912"
                    targetId = "1"
                    enabled = true
                }
            }
            events {
                interactive {
                    id = "912"
                    text = "Should I transform?"
                    option {
                        id = "0"
                        eventId = "352"
                        label = "Yes"
                    }
                    option {
                        id = "1"
                        eventId = InitialValues.NOOP_ID
                        label = "No"
                    }
                }
                swapCharacter {
                    id = "352"
                    enableCharacterId = "2"
                    disableCharacterId = "1"
                    nextEventId = InitialValues.NOOP_ID
                }
            }
        }

        assertNotNull(sceneConfig)
        var targetIndex = 1
        val entityManager = EntityManager(
            map,
            sceneConfig.triggerList,
            sceneConfig.eventList,
            sceneConfig.itemList,
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
            log,
            aiRepo
        )
        val player = sceneConfig.player
        val scene = Scene(entityManager, sceneConfig, log)

        scene.loadScene()
        player.heading = Direction.SOUTH
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)

        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        var enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.group, InitialValues.GROUP_PLAYER)

        targetIndex = 0
        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        enemy = entityManager.getEntity(5, 6)
        assertNotNull(enemy)
        assertEquals(enemy.group, InitialValues.ENEMY_GROUP)
    }

    /**
     * Test GameEntityTrigger without Options
     */
    @Test
    fun gameEntityTriggerWithInteractiveEventWithoutOptions() = runBlockingTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 5
                posY = 5
            }
            entityBuilders {
                ally {
                    id = "1"
                    type = AllyType.SCIENTIST
                }
            }
            entity {
                ally {
                    id = "1"
                    template = "1"
                    group = "0"
                    posX = 5
                    posY = 6
                }
            }
            triggers {
                character {
                    id = "523"
                    eventId = "912"
                    targetId = "1"
                    enabled = true
                }
            }
            events {
                interactive {
                    id = "912"
                    text = "I am happy to see you"
                }
            }
        }

        assertNotNull(sceneConfig)
        val entityManager = EntityManager(
            map,
            sceneConfig.triggerList,
            sceneConfig.eventList,
            sceneConfig.itemList,
            object : EntityManagerEventListener {
                override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {}
                override fun onInteractionRequired(text: String, options: List<InteractiveEventOption>, eventReceiver: EntityManagerInteractionReceiver) {
                    runBlockingTest {
                        assertEquals(0, options.size)
                        eventReceiver.selectOption(null)
                    }
                }
            },
            log,
            aiRepo
        )
        val player = sceneConfig.player
        val scene = Scene(entityManager, sceneConfig, log)

        scene.loadScene()
        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(5, player.posY)

        scene.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
        var scientist = entityManager.getEntity(5, 6) as Ally
        assertNotNull(scientist)
        assertEquals(AllyType.SCIENTIST, scientist.allyType)
    }
}
