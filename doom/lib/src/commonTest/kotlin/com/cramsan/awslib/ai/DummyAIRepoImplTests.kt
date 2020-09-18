package com.cramsan.awslib.ai

import com.cramsan.awslib.ai.implementation.DummyAIRepoImpl
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.logging.EventLoggerInterface
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DummyAIRepoImplTests {

    private lateinit var map: GameMap
    private lateinit var entityManager: EntityManager
    private lateinit var enemy: CharacterInterface
    private lateinit var player: Player
    private lateinit var dummyAIRepoImpl: DummyAIRepoImpl

    @BeforeTest
    fun prepareTest() {
        val log = mockk<EventLoggerInterface>(relaxed = true)
        dummyAIRepoImpl = DummyAIRepoImpl(log)

        map = GameMap(MapGenerator.createMapWithWalls())
        val sceneConfig = scene {
            player {
                posX = 2
                posY = 1
            }
            characters {
                dog {
                    id = 1
                    posX = 1
                    posY = 1
                }
            }
        }
        assertNotNull(sceneConfig)
        entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, null, log, dummyAIRepoImpl)
        enemy = sceneConfig.characterList.first()
        player = sceneConfig.player
        dummyAIRepoImpl = DummyAIRepoImpl(log)
        entityManager.register(enemy)
        entityManager.register(player)
    }

    @Test
    fun getNextActionTest() {
        entityManager.setPosition(player, 1, 1)
        entityManager.setPosition(enemy, 1, 5)
        assertEquals(Direction.NORTH, dummyAIRepoImpl.getNextTurnAction(enemy, entityManager, map).direction)
        assertEquals(TurnActionType.NONE, dummyAIRepoImpl.getNextTurnAction(player, entityManager, map).turnActionType)
    }

    @Test
    fun getNextActionTooFarTest() {
        entityManager.setPosition(player, 1, 1)
        entityManager.setPosition(enemy, 6, 14)
        assertEquals(Direction.KEEP, dummyAIRepoImpl.getNextTurnAction(enemy, entityManager, map).direction)
        assertEquals(TurnActionType.NONE, dummyAIRepoImpl.getNextTurnAction(player, entityManager, map).turnActionType)
    }
}
