package com.cramsan.awslib.entity

import com.cramsan.awslib.entity.implementation.Doctor
import kotlin.test.Test
import kotlin.test.assertEquals

class DoctorTests {

    @Test
    fun accessingAttackTest() {
        val doctor = Doctor(0, 1, 10, 20, 5, true)
        assertEquals(1, doctor.attack)
        doctor.attack = 10
        assertEquals(10, doctor.attack)
        doctor.attack = 0
        assertEquals(0, doctor.attack)
    }
}