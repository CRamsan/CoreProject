package com.cramsan.awslib.entitymanager

import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entity.implementation.Character
import com.cramsan.awslib.entity.implementation.Dog
import com.cramsan.awslib.entity.implementation.GameEntity
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.platform.runTest
import com.cramsan.awslib.utils.map.MapGenerator
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EntityManagerTests {

    private lateinit var map: GameMap
    private lateinit var entityManager: EntityManager

    companion object {
        fun createPlayer(posX: Int, posY: Int): Player {
            return Player(posX, posY, 50)
        }

        fun createCharacter(id: Int, posX: Int, posY: Int): Character {
            return Dog(id, posX, posY, 100, true)
        }
    }

    @BeforeTest
    fun prepareTest() {
        map = GameMap(MapGenerator.createMap100x100())
        entityManager = EntityManager(map, emptyList(), emptyList(), null)
    }

    /**
     * Test registering multiple entities without overlapping
     */
    @Test
    fun registerTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter(i, i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(entityManager.getEntity(i, i), enemy1, "Entity at location $i-$i is not the expected one")
        }
    }

    /**
     * Test registering entities to locations that are already occupied
     */
    @Test
    fun registerWithCollisionTest() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter(i, i, i)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(entityManager.getEntity(i, i), enemy1, "Entity at location $i-$i is not the expected one")
        }

        for (i in 0 until 50) {
            val enemy1 = createCharacter(1 * 100, i, i)
            assertNotNull(entityManager.getEntity(i, i), "Expected location $i-$i is not already occupied")
            assertFalse(entityManager.register(enemy1), "Entity was registered when it shouldn't have")
            assertNotEquals(entityManager.getEntity(i, i), enemy1, "Entity at location $i-$i is not the expected one")
        }
    }

    /**
     * Test registering entities to the locations where they were already registered
     */
    @Test
    fun reregisterTest() {
        var entityList = arrayListOf<GameEntity>()

        for (i in 0 until 50) {
            val scene = scene {
                player {
                }
                entities {
                    dog {
                        id = i
                        posX = i
                        posY = i
                    }
                }
            }
            val enemy1 = scene?.entityList?.first()
            assertNotNull(enemy1)
            assertNull(entityManager.getEntity(i, i), "Expected location $i-$i is already occupied")
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertEquals(entityManager.getEntity(i, i), enemy1, "Entity at location $i-$i is not the expected one")
            assertFalse(entityManager.register(enemy1), "Entity should not have been registered")
            entityList.add(enemy1)
        }

        val posX = 0
        val posY = 1
        for (entity in entityList) {
            assertNull(entityManager.getEntity(posX, posY), "Expected location is already occupied")
            assertFalse(entityManager.register(entity), "Failed to register dog")
            assertNull(entityManager.getEntity(posX, posY), "Entity at location is not the expected one")
        }
    }

    /**
     * Test deregistering entities
     */
    @Test
    fun deregister() {
        for (i in 0 until 50) {
            val enemy1 = createCharacter(1, i, i)
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
            val enemy1 = createCharacter(1, i, i)
            assertTrue(entityManager.register(enemy1), "Failed to register dog")
            assertTrue(entityManager.deregister(enemy1))
            assertFalse(entityManager.deregister(enemy1))
            assertNull(entityManager.getEntity(enemy1.posX, enemy1.posY))
        }
    }

    /**
     * Test running turns and verify properties are updated
     */
    @Test
    fun runTurns() = runTest {
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
        val entity = createCharacter(1, posX, posY)
        assertTrue(entityManager.register(entity))

        // Verify that after calling setPosition the location is updated
        val newPosX = 2
        val newPosY = 2
        assertFalse(entityManager.isBlocked(newPosX, newPosY))
        assertTrue(entityManager.setPosition(entity, newPosX, newPosY))
        assertFalse(entityManager.isBlocked(posX, posY))
        assertTrue(entityManager.isBlocked(newPosX, newPosY))

        val entity2 = createCharacter(2, posX, posY)
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
        val entity = createCharacter(1, posX, posY)
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
        val entity = createCharacter(1, posX, posY)
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
}