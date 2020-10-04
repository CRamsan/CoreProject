package com.cramsan.awslib.entity

import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.AllyType
import kotlin.test.Test
import kotlin.test.assertEquals

class DoctorTests {

    @Test
    fun accessingAttackTest() {
        val doctor = Ally("0", 10, 1, 10, 20, true, AllyType.SCIENTIST)
        assertEquals(1, doctor.attack)
        doctor.attack = 10
        assertEquals(10, doctor.attack)
        doctor.attack = 0
        assertEquals(0, doctor.attack)
    }
}
