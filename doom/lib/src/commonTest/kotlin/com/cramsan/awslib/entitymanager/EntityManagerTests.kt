package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.Character
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.Enemy
import com.cramsan.awslib.entity.implementation.EnemyType
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.utils.map.MapGenerator
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.test.TestBase
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class EntityManagerTests : TestBase() {

    private lateinit var map: GameMap
    private lateinit var entityManager: EntityManager
    private lateinit var log: EventLoggerInterface
    private lateinit var aiRepo: AIRepo

    companion object {

        fun createPlayer(posX: Int, posY: Int): Player {
            return Player(posX, posY, 50)
        }

        fun createCharacter(id: String, posX: Int, posY: Int): Character {
            return Enemy(
                id,
                posX,
                posY,
                100,
                true,
                100,
                EnemyType.DOG,
                10,
                10.0,
                10.0,
                10,
                10
            )
        }

        fun createItem(id: String, posX: Int, posY: Int): ItemInterface {
            return ConsumableItem(id, posX, posY, ConsumableType.HEALTH, 10)
        }
    }

    @BeforeTest
    fun prepareTest() {
        log = mockk<EventLoggerInterface>(relaxed = true)
        aiRepo = mockk<AIRepo>()
        map = GameMap(MapGenerator.createMap100x100())
        entityManager = EntityManager(map, emptyList(), emptyList(), emptyList(), null, log, aiRepo)
    }

    /**
     * Test registering multiple entities without overlapping
     */
    @Test
    fun registerTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("$i", i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }
    }

    /**
     * Test registering entities to locations that are already occupied
     */
    @Test
    fun registerWithCollisionTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("$i", i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }

        for (i in 0 until 50) {
            val enemy1 = createCharacter("${1 * 100}", i, i)
            assertNotNull(
                entityManager.getEntity(i, i),
                "Expected location $i-$i is not already occupied"
            )
            try {
                entityManager.register(enemy1)
                fail("Trying to reregister should throw exception")
            } catch (e: Exception) {
            }
            assertNotEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }
    }

    /**
     * Test registering entities to the locations where they were already registered
     */
    @Test
    fun reregisterTest() {
        var entityList = arrayListOf<CharacterInterface>()

        for (i in 0 until 50) {
            val scene = scene {
                player {
                }
                entityBuilders {
                    enemy {
                        id = "1"
                    }
                }
                entity {
                    enemy {
                        id = "$i"
                        template = "1"
                        posX = i
                        posY = i
                    }
                }
            }
            val enemy1 = scene?.characterList?.first()
            assertNotNull(enemy1)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
            try {
                entityManager.register(enemy1)
                fail("Trying to reregister should throw exception")
            } catch (e: Exception) {
            }
            entityList.add(enemy1)
        }

        val posX = 0
        val posY = 1
        for (entity in entityList) {
            assertNull(entityManager.getEntity(posX, posY), "Expected location is already occupied")
            try {
                entityManager.register(entity)
                fail("Trying to reregister should throw exception")
            } catch (e: Exception) {
            }
            assertNull(
                entityManager.getEntity(posX, posY),
                "Entity at location is not the expected one"
            )
        }
    }

    /**
     * Test deregistering entities
     */
    @Test
    fun deregister() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("1", i, i)
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertTrue(entityManager.deregister(enemy1))
            assertNull(entityManager.getEntity(enemy1.posX, enemy1.posY))
        }
    }

    /**
     * Test deregistering entities that were already deregistered
     */
    @Test
    fun rederegister() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("1", i, i)
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertTrue(entityManager.deregister(enemy1))
            assertFalse(entityManager.deregister(enemy1))
            assertNull(entityManager.getEntity(enemy1.posX, enemy1.posY))
        }
    }

    /**
     * Test registering multiple entities without overlapping
     */
    @Test
    fun registerItemTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("$i", i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }
    }

    /**
     * Test registering entities to locations that are already occupied
     */
    @Test
    fun registerItemWithCollisionTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter("$i", i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }

        for (i in 0 until 50) {
            val enemy1 = createCharacter("${1 * 100}", i, i)
            assertNotNull(
                entityManager.getEntity(i, i),
                "Expected location $i-$i is not already occupied"
            )
            try {
                entityManager.register(enemy1)
                fail("Trying to reregister should throw exception")
            } catch (e: Exception) {
            }
            assertNotEquals(
                entityManager.getEntity(i, i),
                enemy1,
                "Entity at location $i-$i is not the expected one"
            )
        }
    }

    /**
     * Test running turns and verify properties are updated
     */
    @Test
    fun runTurns() = runBlockingTest {
        val player = createPlayer(1, 1)
        assertTrue(entityManager.register(player))

        player.heading = Direction.SOUTH
        for (i in 0 until 10) {
            assertEquals(player.nextTurnAction.turnActionType, TurnActionType.NONE)
            assertEquals(player.heading, Direction.SOUTH)
            player.nextTurnAction = TurnAction(TurnActionType.MOVE, Direction.SOUTH)
            val posX = player.posX
            val posY = player.posY
            entityManager.runTurns(null)
            assertFalse(entityManager.isBlocked(posX, posY))
            assertEquals(entityManager.getEntity(player.posX, player.posY), player)
        }
        assertEquals(player.posX, 1)
        assertEquals(player.posY, 11)
    }

    /**
     * Test using setPosition and verify position is correclty updated
     */
    @Test
    fun setPosition() {
        val posX = 5
        val posY = 5
        val entity = createCharacter("1", posX, posY)
        assertTrue(entityManager.register(entity))

        // Verify that after calling setPosition the location is updated
        val newPosX = 2
        val newPosY = 2
        assertFalse(entityManager.isBlocked(newPosX, newPosY))
        assertTrue(entityManager.setPosition(entity, newPosX, newPosY))
        assertFalse(entityManager.isBlocked(posX, posY))
        assertTrue(entityManager.isBlocked(newPosX, newPosY))

        val entity2 = createCharacter("2", posX, posY)
        assertTrue(entityManager.register(entity2))
        assertFalse(entityManager.setPosition(entity2, newPosX, newPosY))
        assertTrue(entityManager.isBlocked(posX, posY))

        // Verify that we cannot set the position to an ocupied location
        assertFalse(entityManager.setPosition(entity, newPosX, newPosY))
        assertTrue(entityManager.isBlocked(newPosX, newPosY))
    }

    /**
     * Test that getEntity returns the expected dog
     */
    @Test
    fun getEntity() {
        val posX = 5
        val posY = 5
        assertNull(entityManager.getEntity(posX, posY))
        val entity = createCharacter("1", posX, posY)
        assertTrue(entityManager.register(entity))
        assertEquals(entityManager.getEntity(posX, posY), entity)

        // Verify that after calling setPosition the location is updated
        val newPosX = 2
        val newPosY = 2
        assertNull(entityManager.getEntity(newPosX, newPosY))
        assertTrue(entityManager.setPosition(entity, newPosX, newPosY))
        assertNull(entityManager.getEntity(posX, posY))
        assertEquals(entityManager.getEntity(newPosX, newPosY), entity)
    }

    @Test
    fun isBlocked() {
        val posX = 5
        val posY = 5
        // Verify that registering an dog sets the location as blocked
        assertFalse(entityManager.isBlocked(posX, posY))
        val entity = createCharacter("1", posX, posY)
        assertFalse(entityManager.isBlocked(posX, posY))
        assertTrue(entityManager.register(entity), "Failed to register dog")
        assertTrue(entityManager.isBlocked(posX, posY))

        // Verify that is blocked can handle an dog after it was moved
        val newPosX = 2
        val newPosY = 2
        assertFalse(entityManager.isBlocked(newPosX, newPosY))
        assertTrue(entityManager.setPosition(entity, newPosX, newPosY))
        assertFalse(entityManager.isBlocked(posX, posY))
        assertTrue(entityManager.isBlocked(newPosX, newPosY))
    }

    /**
     * Test picking up items
     * TODO: Reenable this test
     */
    @Ignore
    @Test
    fun playerPicksUpItemTest() = runBlockingTest {
        val map = GameMap(MapGenerator.createMap100x100())

        val sceneConfig = scene {
            player {
                posX = 5
                posY = 5
            }
            itemBuilders {
                consumable {
                    id = "1"
                }
            }
            items {
                consumable {
                    template = "1"
                    posX = 5
                    posY = 6
                }
            }
        }
        assertNotNull(sceneConfig)
        val entityManager = EntityManager(
            map,
            sceneConfig.triggerList,
            sceneConfig.eventList,
            sceneConfig.itemList,
            null,
            log,
            aiRepo
        )
        val player = sceneConfig.player

        val scene = Scene(entityManager, sceneConfig, log)
        scene.loadScene()

        scene.runTurn(TurnAction(TurnActionType.MOVE, Direction.SOUTH))
        assertEquals(5, player.posX)
        assertEquals(6, player.posY)

        assertNull(entityManager.getItem(5, 6), "Item should disappear when touched")
    }
}
