package com.cramsan.awslib.entity

import com.cramsan.awslib.entity.implementation.Player
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerTests {

    @Test
    fun accessingAttackTest() {
        val player = Player(0, 1, 10)
        assertEquals(0, player.attack)
        player.attack = 10
        assertEquals(10, player.attack)
        player.attack = 0
        assertEquals(0, player.attack)
    }
}
