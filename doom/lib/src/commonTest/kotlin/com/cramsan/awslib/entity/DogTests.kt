package com.cramsan.awslib.entity

import com.cramsan.awslib.entity.implementation.Enemy
import com.cramsan.awslib.entity.implementation.EnemyType
import kotlin.test.Test
import kotlin.test.assertEquals

class DogTests {

    /**
     * Test registering multiple entities without overlapping
     */
    @Test
    fun accessingAttackTest() {
        val dog = Enemy(
            "0",
            1,
            10,
            5,
            true,
            100,
            EnemyType.DOG,
            10,
            10.0,
            10.0,
            10,
            10
        )
        assertEquals(5, dog.attack)
        dog.attack = 10
        assertEquals(10, dog.attack)
        dog.attack = 0
        assertEquals(0, dog.attack)
    }
}
