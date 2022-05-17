package me.cesar.application.service

import com.cramsan.framework.test.TestBase
import com.fasterxml.jackson.core.JsonGenerator
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test

/**
 * @author cramsan
 */
class KotlinInstantSerializerTest : TestBase() {

    lateinit var serializer: KotlinInstantSerializer

    @RelaxedMockK
    lateinit var jgen: JsonGenerator

    override fun setupTest() {
        serializer = KotlinInstantSerializer()
    }

    @Test
    fun `Testing serializing distant past`() {
        val input = Instant.DISTANT_PAST

        serializer.serialize(input, jgen, mockk())

        verify { jgen.writeNumber(-3217862419200001) }
        confirmVerified(jgen)
    }

    @Test
    fun `Testing serializing distant future`() {
        val input = Instant.DISTANT_FUTURE

        serializer.serialize(input, jgen, mockk())

        verify { jgen.writeNumber(3093527980800000) }
        confirmVerified(jgen)
    }

    @Test
    fun `Testing serializing date`() {
        val input = LocalDateTime(
            2016,
            2,
            15,
            16,
            57
        ).toInstant(TimeZone.UTC)

        serializer.serialize(input, jgen, mockk())

        verify { jgen.writeNumber(1455555420000) }
        confirmVerified(jgen)
    }

    @Test
    fun `Testing serializing date with time zone`() {
        val input = LocalDateTime(
            2022,
            5,
            15,
            17,
            23,
        ).toInstant(TimeZone.of("UTC+3"))

        serializer.serialize(input, jgen, mockk())

        verify { jgen.writeNumber(1652624580000) }
        confirmVerified(jgen)
    }
}