package com.cramsan.awslib.entity

import com.cramsan.awslib.entity.implementation.Dog
import kotlin.test.Test
import kotlin.test.assertEquals

class DogTests {

    /**
     * Test registering multiple entities without overlapping
     */
    @Test
    fun accessingAttackTest() {
        val dog = Dog(0, 1, 10, 5, true)
        assertEquals(5, dog.attack)
        dog.attack = 10
        assertEquals(10, dog.attack)
        dog.attack = 0
        assertEquals(0, dog.attack)
    }
}